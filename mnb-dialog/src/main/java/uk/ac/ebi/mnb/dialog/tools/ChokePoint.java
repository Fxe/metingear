/**
 * FindChokePoints.java
 *
 * 2011.10.03
 *
 * This file is part of the CheMet library
 * 
 * The CheMet library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * CheMet is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with CheMet.  If not, see <http://www.gnu.org/licenses/>.
 */
package uk.ac.ebi.mnb.dialog.tools;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JFrame;
import org.apache.log4j.Logger;
import uk.ac.ebi.core.MetabolicReaction;
import uk.ac.ebi.core.Metabolite;
import uk.ac.ebi.mnb.core.GeneralAction;
import uk.ac.ebi.mnb.core.ContextAction;
import uk.ac.ebi.mnb.core.Utilities;
import uk.ac.ebi.mnb.interfaces.MainController;
import uk.ac.ebi.mnb.interfaces.SelectionController;
import uk.ac.ebi.mnb.interfaces.ViewController;
import uk.ac.ebi.mnb.view.SelectionDialog;

/**
 * @name    FindChokePoints - 2011.10.03 <br>
 *          Performs an action on the given context (selection)
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ChokePoint extends ContextAction {

    private static final Logger LOGGER = Logger.getLogger(ChokePoint.class);

    public ChokePoint(MainController controller) {
        super(ChokePoint.class.getSimpleName(), controller);
    }

    public void actionPerformed(ActionEvent ae) {
        Map<Metabolite, Integer> rMap = new HashMap();
        Map<Metabolite, Integer> pMap = new HashMap();

        for (MetabolicReaction rxn : Utilities.getReactions(getSelection())) {
            for (Metabolite m : rxn.getReactantMolecules()) {
                rMap.put(m, rMap.containsKey(m) ? rMap.get(m) + 1 : 1);
            }
            for (Metabolite m : rxn.getProductMolecules()) {
                pMap.put(m, pMap.containsKey(m) ? pMap.get(m) + 1 : 1);
            }
        }

        List<Metabolite> chokePoints = new ArrayList();

        for (Entry<Metabolite, Integer> e : rMap.entrySet()) {
            if (e.getValue() == 1 && pMap.containsKey(e.getKey()) && pMap.get(e.getKey()) == 1) {
                chokePoints.add(e.getKey());
            }
        }

        setSelection(chokePoints);

    }
}
