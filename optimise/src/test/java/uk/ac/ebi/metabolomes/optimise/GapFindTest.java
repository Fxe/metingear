/*
 * Copyright (c) 2013. EMBL, European Bioinformatics Institute
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.metabolomes.optimise;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.ac.ebi.mdk.domain.matrix.BasicStoichiometricMatrix;
import uk.ac.ebi.optimise.SimulationUtil;

import java.util.logging.Level;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

/** @author johnmay */
public class GapFindTest {
//    private static final Logger LOGGER = Logger.getLogger(GapFindTest.class);
//
//    private        BasicStoichiometricMatrix s;
//    private static Boolean                   runnable;
//
//    @BeforeClass
//    public static void setup() {
//        runnable = SimulationUtil.setup();
//    }
//
//    @Before
//    public void setupMatrix() {
//        s = BasicStoichiometricMatrix.create();
//
//        // internal reactions
//        s.addReaction("A => B", false);
//        s.addReaction("B => C", false);
//        s.addReaction("C => D", false);
//        s.addReaction("D => E", false);
//    }
//
//    /**
//     * Tests fig.1 holds from BMC Bioinformatics 2007, 8:212
//     * http://www.biomedcentral.com/1471-2105/8/212
//     *
//     * @throws Exception
//     */
//    @Test public void paperFig1Test_a() throws Exception {
//
//        if (!runnable) return;
//
//        BasicStoichiometricMatrix s2 = BasicStoichiometricMatrix.create(5, 5);
//        s2.addReaction("A => ?", false);
//        s2.addReaction("A => C", false);
//
//        // exchange reactions
//        s2.addProduction("?", false);
//        s2.addConsumption("?", false);
//        s2.addConsumption("C", false);
//        s2.display();
//
//        GapFind gf = new GapFind(s2);
//        System.out.println("Non-production:");
//        for (Integer i : gf.getUnproducedMetabolites()) {
//            System.out.print(s2.getMolecule(i) + "\t");
//            System.out.println(gf.isProduced(i) ? "" : "*");
//        }
//        System.out.println("Non-consumption:");
//        for (Integer i : gf.getUnconsumedMetabolites()) {
//            System.out.print(s2.getMolecule(i) + "\t");
//            System.out.println(gf.isConsumed(i) ? "" : "*");
//        }
//
//
//    }
//
//    @Test public void paperFig1Test_b() throws Exception {
//
//        if (!runnable) return;
//
//        BasicStoichiometricMatrix s2 = BasicStoichiometricMatrix.create(5, 5);
//        s2.addReaction("D => B", false);
//        s2.addReaction("? => B", false);
//
//        // exchange reactions
//        s2.addProduction("?", false);
//        s2.addProduction("D", false);
//        s2.addConsumption("?", false);
//
//        s2.display();
//
//        GapFind gf = new GapFind(s2);
//        System.out.println("Non-production:");
//        for (Integer i : gf.getUnproducedMetabolites()) {
//            System.out.print(s2.getMolecule(i) + "\t");
//            System.out.println(gf.isProduced(i) ? "" : "*");
//        }
//        System.out.println("Non-consumption:");
//        for (Integer i : gf.getUnconsumedMetabolites()) {
//            System.out.print(s2.getMolecule(i) + "\t");
//            System.out.println(gf.isConsumed(i) ? "" : "*");
//        }
//
//
//    }
//
//    /** Test of findNonProductionMetabolites method, of class DeadEndDetector. */
//    @Test
//    public void testFindNonProductionMetabolites()
//            throws Exception {
//
//        if (!runnable) return;
//
//        // drain E and F
//        s.addReaction(new String[]{"E"},
//                      new String[0]);
//
//        GapFind gf = new GapFind(s);
//        Integer[] unproduced = gf.getUnproducedMetabolites();
//
//        assertEquals(4, unproduced.length);
//
//        assertThat(unproduced, is(new Integer[]{0, 1, 2, 3}));
//
//        // allow influx of A
//        s.addReaction(new String[0],
//                      new String[]{"A"});
//        s.display();
//        gf = new GapFind(s);
//        unproduced = gf.getUnproducedMetabolites();
//
//        assertEquals(0, unproduced.length);
//    }
//
//    /** Test of findNonConsumptionMetabolites method, of class DeadEndDetector. */
//    @Test
//    public void testFindNonConsumptionMetabolites()
//            throws Exception {
//        if (!runnable) return;
//
//        // allow influx of A
//        s.addReaction(new String[0],
//                      new String[]{"A"});
//
//        GapFind gf = new GapFind(s);
//        Integer[] unconsumed = gf.getUnconsumedMetabolites();
//
//        assertEquals(4, unconsumed.length);
//
//        for (Integer i = 1; i < unconsumed.length; i++) {
//            assertEquals(unconsumed[i - 1], i);
//        }
//
//        // drain E
//        s.addReaction(new String[]{"E"},
//                      new String[0]);
//        s.addReaction(new String[]{"F"}, new String[0]);
//        gf = new GapFind(s);
//        unconsumed = gf.getUnconsumedMetabolites();
//
//        assertEquals(0, unconsumed.length);
//    }
//
//    /** Test of getTerminalNCMetabolites method, of class DeadEndDetector. */
//    @Test
//    public void testRoot() {
//        if (!runnable) return;
//        try {
//            GapFind gf = new GapFind(s);
//            Integer[] root = gf.getRootUnproducedMetabolites();
//
//            s.display();
//
//            // there should be 2 root non-production metabolites, E and C
//            assertEquals(1, root.length);
//            assertEquals("A",
//                         s.getMolecule(root[0]));
//
//            // product A via exchange reaction
//            s.addReaction(new String[0],
//                          new String[]{"A"});
//            gf = new GapFind(s);
//            root = gf.getRootUnproducedMetabolites();
//
//        } catch (UnsatisfiedLinkError ex) {
//            java.util.logging.Logger.getLogger(GapFindTest.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (Exception ex) {
//            java.util.logging.Logger.getLogger(GapFindTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    /** Test of getRootNPMetabolites method, of class DeadEndDetector. */
//    @Test
//    public void testTerminal() {
//        if (!runnable) return;
//        try {
//            GapFind gf = new GapFind(s);
//            Integer[] terminal = gf.getTerminalUnconsumpedMetabolites();
//
//            // check there should be 2 root non-production metabolites, A and B
//            assertEquals(terminal.length, 1);
//            assertEquals("E",
//                         s.getMolecule(terminal[0]));
//
//            //  produce A via exchange reaction
//            s.addReaction(new String[]{"E"},
//                          new String[0]);
//            gf = new GapFind(s);
//            terminal = gf.getTerminalUnconsumpedMetabolites();
//
//            // there should be no NP dead end metabolites
//            assertEquals(terminal.length, 0);
//        } catch (UnsatisfiedLinkError ex) {
//            java.util.logging.Logger.getLogger(GapFindTest.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (Exception ex) {
//            java.util.logging.Logger.getLogger(GapFindTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
}
