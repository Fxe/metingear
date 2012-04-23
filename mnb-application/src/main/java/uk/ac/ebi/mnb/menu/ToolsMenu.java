/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mnb.menu;

import org.apache.log4j.Logger;
import uk.ac.ebi.core.MetabolicReactionImplementation;
import uk.ac.ebi.interfaces.entities.EntityCollection;
import uk.ac.ebi.interfaces.entities.Reconstruction;
import uk.ac.ebi.interfaces.entities.Metabolite;
import uk.ac.ebi.interfaces.entities.ProteinProduct;
import uk.ac.ebi.mdk.domain.tool.ReconstructionManager;
import uk.ac.ebi.metingeer.interfaces.menu.ContextResponder;
import uk.ac.ebi.mnb.dialog.tools.*;
import uk.ac.ebi.mnb.dialog.tools.stoichiometry.CreateMatrix;
import uk.ac.ebi.mnb.main.MainView;

import javax.swing.*;


/**
 * FileMenu.java
 *
 *
 * @author johnmay @date Apr 28, 2011
 */
public class ToolsMenu extends ContextMenu {

    private static final Logger logger = Logger.getLogger(ToolsMenu.class);

    private GapAnalysis gapMenu;


    public ToolsMenu() {

        super("Tools", MainView.getInstance());

        MainView view = MainView.getInstance();

        add(create(AutomaticCrossReference.class), new ContextResponder() {

            public boolean getContext(ReconstructionManager reconstructions, Reconstruction active, EntityCollection selection) {
                return selection.hasSelection(Metabolite.class);
            }
        });

        add(new CuratedReconciliation(view), new ContextResponder() {

            public boolean getContext(ReconstructionManager reconstructions, Reconstruction active, EntityCollection selection) {
                return selection.hasSelection(Metabolite.class);
            }
        });

        add(create(DownloadStructuresDialog.class), new ContextResponder() {

            public boolean getContext(ReconstructionManager reconstructions, Reconstruction active, EntityCollection selection) {
                return selection.hasSelection(Metabolite.class);
            }
        });

        add(new JSeparator());

        add(new ChokePoint(view), new ContextResponder() {

            public boolean getContext(ReconstructionManager reconstructions, Reconstruction active, EntityCollection selection) {
                return selection.hasSelection(MetabolicReactionImplementation.class);
            }
        });

        //add(new AssignReactions(view));

        add(new JSeparator());

        /**
         * *********************
         * Sequence annotation *
         **********************
         */
        add(create(SequenceHomology.class), new ContextResponder() {

            public boolean getContext(ReconstructionManager reconstructions, Reconstruction active, EntityCollection selection) {
                return selection.hasSelection(ProteinProduct.class);
            }
        });
        add(create(TransferAnnotations.class), new ContextResponder() {

            public boolean getContext(ReconstructionManager reconstructions, Reconstruction active, EntityCollection selection) {
                return selection.hasSelection(ProteinProduct.class);
            }
        });

        add(new JSeparator());

        /**
         * *********************
         * Merging *
         **********************
         */
        add(new JMenuItem(new MergeLoci(MainView.getInstance())));

        add(create(CollapseStructures.class), new ContextResponder() {

            public boolean getContext(ReconstructionManager reconstructions, Reconstruction active, EntityCollection selection) {
                return selection.hasSelection(Metabolite.class);

            }
        });

        add(new JSeparator());
        /**
         * ***********************
         * Stoichiometric Matrix *
         ************************
         */
        add(create(CreateMatrix.class), new ContextResponder() {

            public boolean getContext(ReconstructionManager reconstructions, Reconstruction active, EntityCollection selection) {
                return selection.hasSelection(MetabolicReactionImplementation.class) || (active != null && active.getReactome().isEmpty() == false);
            }
        });

        gapMenu = new GapAnalysis(view);
        add(gapMenu);

        /**
         * ***********************
         * Comparisson *
         ************************
         */
        add(new JSeparator());

        add(create(CompareReconstruction.class), new ContextResponder() {

            public boolean getContext(ReconstructionManager reconstructions, Reconstruction active, EntityCollection selection) {
                return reconstructions.getProjects().size() > 1;
            }
        });

    }


    @Override
    public void updateContext() {
        super.updateContext();
        gapMenu.updateContext();
    }
}
