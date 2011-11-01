/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uk.ac.ebi.mnb.menu;

import javax.swing.JComponent;
import javax.swing.JSeparator;
import uk.ac.ebi.mnb.menu.build.PredictGPR;
import uk.ac.ebi.mnb.menu.build.CatFamAction;
import uk.ac.ebi.mnb.menu.build.PriamAction;
import uk.ac.ebi.mnb.menu.build.StoichiometricMatixAction;
import uk.ac.ebi.mnb.menu.build.StoichiometryAction;
import uk.ac.ebi.mnb.menu.build.SwissProtHomology;
import uk.ac.ebi.visualisation.ViewUtils;

/**
 * FileMenu.java
 *
 *
 * @author johnmay
 * @date Apr 13, 2011
 */
public class BuildMenu
    extends ClearMenu {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( BuildMenu.class );
    private SwissProtHomology enzymeHomologyAction;
    private JComponent items[] = new JComponent[ 8 ];

    public BuildMenu() {

        super( "Build" );
        setBackground(ViewUtils.CLEAR_COLOUR);
        setBorderPainted(false);

        enzymeHomologyAction = new SwissProtHomology();
        int index = 0;
        items[index++] = new DynamicMenuItem( enzymeHomologyAction );
        items[index++] = new DynamicMenuItem( new PredictGPR() );
        items[index++] = new DynamicMenuItem( new PriamAction() );
        items[index++] = new DynamicMenuItem( new CatFamAction() );
        items[index++] = new JSeparator();
        items[index++] = new DynamicMenuItem( new StoichiometryAction() );
        items[index++] = new DynamicMenuItem( new StoichiometricMatixAction() );
        items[index++] = new JSeparator();

        for ( JComponent component : items ) {
            add( component );
            if ( component instanceof DynamicMenuItem ) {
                ( ( DynamicMenuItem ) component ).reloadEnabled();
            }
        }
    }

    public SwissProtHomology getEnzymeHomologyDialogAction() {
        return enzymeHomologyAction;
    }

    public void setActiveDependingOnRequirements() {
        for ( JComponent component : items ) {
            if ( component instanceof DynamicMenuItem ) {
                ( ( DynamicMenuItem ) component ).reloadEnabled();
            }
        }
    }
}
