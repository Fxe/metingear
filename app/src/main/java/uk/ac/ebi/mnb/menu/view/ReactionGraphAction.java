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
package uk.ac.ebi.mnb.menu.view;

import java.awt.event.ActionEvent;
import uk.ac.ebi.caf.action.GeneralAction;

/**
 * ReactionGraphAction.java
 *
 *
 * @author johnmay
 * @date May 27, 2011
 */
public class ReactionGraphAction
        extends GeneralAction {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( ReactionGraphAction.class );

    public ReactionGraphAction() {
        super( "ReactionGraph" );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
        throw new UnsupportedOperationException("Old method");
//        //SelectionManager selectionManager = MainController.getInstance().getSelectionManager();
//        //GeneProductSelection selection = selectionManager.getGeneProductSelection();
//        GeneProduct[] products = ReconstructionManager.getInstance().getActiveReconstruction().getGeneProducts().getAllProducts();
//        System.out.println( products.length );
//        List<Reaction> reactionsToShow = new ArrayList();
//        for ( GeneProduct product : products ) {
//            if ( !product.getReactions().isEmpty() ) {
//                reactionsToShow.addAll( product.getReactions() );
//            }
//        }
//        GraphPanel gp = new GraphPanel();
//        MainFrame.getInstance().getViewController().add(gp, "Reaction Graph");
//        ((CardLayout)MainFrame.getInstance().getViewController().getLayout()).show( MainFrame.getInstance().getViewController() , "Reaction Graph");
//        ReactionGraph graph = new ReactionGraph();
//        graph.addReactions( reactionsToShow );
//        gp.setModel( graph );
//        gp.buildGraph();
//        gp.revalidate();
//        gp.repaint();

    }
}
