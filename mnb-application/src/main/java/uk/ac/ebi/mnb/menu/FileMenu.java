/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mnb.menu;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import uk.ac.ebi.mnb.menu.file.ImportSBMLAction;
import uk.ac.ebi.mnb.menu.file.SaveAsProjectAction;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import uk.ac.ebi.core.ReconstructionManager;
import uk.ac.ebi.mnb.core.GeneralAction;
import uk.ac.ebi.mnb.core.WarningMessage;
import uk.ac.ebi.mnb.main.MainView;
import uk.ac.ebi.mnb.menu.file.ExportSIFAction;
import uk.ac.ebi.mnb.menu.file.ExportSBMLAction;
import uk.ac.ebi.mnb.menu.file.ImportPeptidesAction;
import uk.ac.ebi.mnb.menu.file.ImportXLSAction;
import uk.ac.ebi.mnb.menu.file.NewProjectAction;
import uk.ac.ebi.mnb.menu.file.OpenProjectAction;
import uk.ac.ebi.mnb.menu.file.SaveProjectAction;
import uk.ac.ebi.mnb.menu.popup.CloseProject;

/**
 * FileMenu.java
 *
 *
 * @author johnmay
 * @date Apr 13, 2011
 */
public class FileMenu
        extends ClearMenu {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(FileMenu.class);
    private SaveAsProjectAction saveAs = new SaveAsProjectAction();
    private NewProjectAction newProjectAction = new NewProjectAction();
    private JMenu recent = new JMenu("Open Recent...");

    public FileMenu() {
        super("File");
        add(new DynamicMenuItem(newProjectAction));
        add(new DynamicMenuItem(new OpenProjectAction(this)));
        add(recent);
        add(new JSeparator());
        add(new JMenuItem(new CloseProject(true)));
        add(new DynamicMenuItem(new SaveProjectAction()));
        add(new DynamicMenuItem(saveAs));
        add(new JSeparator());
        add(new ImportMenu());
        add(new DynamicMenuItem(new ImportSBMLAction()));
        add(new DynamicMenuItem(new ImportXLSAction()));
        add(new JSeparator());
        add(new ExportMenu());
        add(new ExportSBMLAction());


        rebuildRecentlyOpen();

    }
    private Color ACTIVE_TOP_GRADIENT_COLOR = new Color(0xc8c8c8);
    private Color ACTIVE_BOTTOM_GRADIENT_COLOR = new Color(0xbcbcbc);
    private Color INACTIVE_TOP_GRADIENT_COLOR = new Color(0xe9e9e9);
    private Color INACTIVE_BOTTOM_GRADIENT_COLOR = new Color(0xe4e4e4);

    public void rebuildRecentlyOpen() {
        recent.removeAll(); // could just add and remove items... but for now
        Set<String> seen = new HashSet<String>();
        Stack<String> items = ReconstructionManager.getInstance().getInstance().getRecent();
        Stack<String> unique = new Stack<String>();
        for (int i = items.size() - 1; i >= 0; i--) {
            if (seen.contains(items.get(i)) == false) {
                String path = items.get(i);
                File file = new File(path);
                if (file.exists()) {
                    recent.add(new JMenuItem(new OpenProjectAction(this, file)));
                }
                seen.add(path);
                unique.push(path);
            }
        }
        ReconstructionManager.getInstance().getInstance().setRecent(unique);

    }

//    @Override
//    protected void paintBorder( Graphics g ) {
//        //super.paintBorder( g );
//    }
//
//    @Override
//    protected void paintComponent( Graphics g ) {
//        Graphics2D g2 = ( Graphics2D ) g;
//        boolean containedInActiveWindow = WindowUtils.isParentWindowFocused( this );
//        Color topColor = containedInActiveWindow
//                         ? ACTIVE_TOP_GRADIENT_COLOR : INACTIVE_TOP_GRADIENT_COLOR;
//        Color bottomColor = containedInActiveWindow
//                            ? ACTIVE_BOTTOM_GRADIENT_COLOR : INACTIVE_BOTTOM_GRADIENT_COLOR;
//        GradientPaint paint = new GradientPaint( 0 , 1 , topColor , 0 , getHeight() , bottomColor );
//        g2.setPaint( paint );
//        g2.fillRect( 0 , 0 , getWidth() , getHeight() );
//        super.paintComponent( g );
//    }
    public NewProjectAction getNewProjectAction() {
        return newProjectAction;
    }

    public void promptForSave() {
        saveAs.activateActions();
    }

    /**
     * Import sub menu of File
     */
    private class ImportMenu extends JMenu {

        public ImportMenu() {

            super("Import...");
            add(new DynamicMenuItem(new ImportPeptidesAction()));
        }
    }

    /**
     * Export sub menu of File
     */
    private class ExportMenu extends JMenu {

        public ExportMenu() {
            super("Export...");
            add(new DynamicMenuItem(new ExportSIFAction()));
            add(new JMenuItem("Selected Metabolites (.mdl)"));
            add(new JMenuItem("Selected Metabolites (.sbml)"));
            add(new JMenuItem("Selected Proteins (.fasta)"));
            add(new JMenuItem("Selected Reactions (.sbml)"));
            add(new JMenuItem("Selected Reactions (.rxn)"));
        }
    }
}
