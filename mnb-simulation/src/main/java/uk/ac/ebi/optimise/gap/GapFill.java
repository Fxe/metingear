/**
 * GapFill.java
 *
 * 2012.01.10
 *
 * This file is part of the CheMet library
 *
 * The CheMet library is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * CheMet is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet. If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.optimise.gap;

import com.google.common.collect.BiMap;
import ilog.concert.IloException;
import ilog.concert.IloIntVar;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import uk.ac.ebi.metabolomes.core.reaction.matrix.BasicStoichiometricMatrix;
import uk.ac.ebi.metabolomes.core.reaction.matrix.StoichiometricMatrix;
import uk.ac.ebi.optimise.CPLEXConstraints;
import uk.ac.ebi.optimise.SimulationUtil;


/**
 *
 * GapFill 2012.01.10
 *
 * @version $Rev$ : Last Changed $Date: 2012-01-10 13:15:20 +0000 (Tue,
 * 10 Jan 2012) $
 * @author johnmay
 * @author $Author$ (this version)
 *
 * Class description
 *
 */
public class GapFill<M, R> {

    private static final Logger LOGGER = Logger.getLogger(GapFill.class);

    private StoichiometricMatrix<M, R> database;

    private StoichiometricMatrix<M, R> model;

    private StoichiometricMatrix<M, R> combined;

    /**
     * Bi-directional hash maps provide look-up of database/model reaction index
     * in the combined matrix and vise versa
     */
    private BiMap<Integer, Integer> databaseMap;

    private BiMap<Integer, Integer> modelMap;

    private IloCplex solver;

    /**
     *
     * Linear programming variables
     *
     * w: binary matrix indicates if reaction j produces metabolite i y: binary
     * vector indicates whether database reaction j resolves gap v: flux vector
     *
     */
    private IloIntVar[][] w;

    private IloIntVar[] y;

    private IloNumVar[] v;


    /**
     *
     * @param database
     * @param model
     * @throws IloException
     * @throws UnsatisfiedLinkError thrown if libray.path is not setup correct
     */
    public GapFill(StoichiometricMatrix<M, R> database,
                   StoichiometricMatrix<M, R> model) throws IloException, UnsatisfiedLinkError {

        this.database = database;
        this.model = model;

        this.combined = database.newInstance(database.getMoleculeCount(),
                                             database.getReactionCount());

        databaseMap = combined.assign(database);
        modelMap = combined.assign(model);

        // remove intersect from database
        for (Integer j : modelMap.values()) {
            databaseMap.remove(j);
        }

        SimulationUtil.setup();

        this.solver = new IloCplex();


        // intialise variables here...        
        v = solver.numVarArray(combined.getReactionCount(), -100, 100);
        y = solver.boolVarArray(combined.getReactionCount()); // - modelMap.size()
        w = new IloIntVar[combined.getMoleculeCount()][combined.getReactionCount()];
        for (int i = 0; i < combined.getMoleculeCount(); i++) {
            w[i] = solver.boolVarArray(combined.getReactionCount());
        }

        // intialise constraints

        // constrain flux
        for (int j = 0; j < v.length; j++) {
            // model
            if (modelMap.containsValue(j)) {
                solver.addLe(v[j], 100);
                solver.addGe(v[j], 0);
            } // database
            else {
                solver.addLe(v[j], solver.prod(y[j], 100));
                solver.addGe(v[j], solver.prod(y[j], -1000));
            }
        }

        solver.add(CPLEXConstraints.getPositiveMassBalance(combined, v));

    }


    /**
     *
     * Access the column indices (database) of reactions that can resolve
     * dead-end metabolite at row index 'i' (model)
     *
     *
     * @return
     *
     */
    public List<Integer> getCandidates(int i) throws IloException {

        int index = combined.getIndex(model.getMolecule(i));

        System.out.println("Model:" + model.getMolecule(i));
        System.out.println("Combined:" + combined.getMolecule(index));

        solver.add(CPLEXConstraints.getProductionConstraints(combined, v, w, index));

        solver.addMinimize(solver.sum(y));

        boolean solved = solver.solve();

        List<Integer> candidates = new ArrayList<Integer>();

        if (solved) {

            double[] solutions = solver.getValues(y);

            for (int j = 0; j < solutions.length; j++) {
                System.out.println(solutions[j]);
                if (solutions[j] == 1.0d) {
                    candidates.add(databaseMap.get(j));
                }
            }

        }

        return candidates;

    }


    public List<R> getCandidateReactions(int i) throws IloException {
        List<R> rxns = new ArrayList<R>();
        for (Integer j : getCandidates(i)) {
            rxns.add(database.getReaction(j));
        }
        return rxns;
    }


    public static void main(String[] args) throws IloException {

        SimulationUtil.setup();

        BasicStoichiometricMatrix model = BasicStoichiometricMatrix.create();

        model.addReaction(new String[0], new String[]{"A"}, true);
        //       model.addReaction(new String[]{"E"}, new String[0], false);

        model.addReaction("A => B", false);
        model.addReaction("B => C", false);
        model.addReaction("B => D", false);
        //model.addReaction("D => E", false);
        model.addReaction("E => C", false);

        model.addReaction(new String[]{"C"}, new String[0], true);

        //model.addReaction("B => D", false);
//        model.addReaction("D => E", false);
        //model.addReaction("E => F", false);
        //model.addReaction("F => G", false);
        //model.addReaction("G => C", false);

        model.display();

        System.out.println("Unconsumed metabolites:");
        for (int i : new GapFind(model).getUnconsumedMetabolites()) {
            System.out.println(model.getMolecule(i));
        }
        System.out.println("Unproduced metabolites:");
        for (int i : new GapFind(model).getUnproducedMetabolites()) {
            System.out.println(model.getMolecule(i));
        }
        for (int i : new GapFind(model).getRootUnproducedMetabolites()) {
            System.out.println(model.getMolecule(i));
        }


        BasicStoichiometricMatrix reference = BasicStoichiometricMatrix.create();

        reference.addReactionWithName("db4", "J => F");
        reference.addReactionWithName("db3", "D => E");
        reference.addReactionWithName("db1", "I => F");
        reference.addReactionWithName("db1", "E => F");
        reference.addReactionWithName("db2", "E => I");


        reference.display();

        GapFill<String, String> gf = new GapFill<String, String>(reference, model);

        System.out.println(gf.getCandidateReactions(model.getIndex("E")));

    }
}