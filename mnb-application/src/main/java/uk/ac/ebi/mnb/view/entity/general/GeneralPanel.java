/**
 * MetabolitePanel.java
 *
 * 2011.09.30
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
package uk.ac.ebi.mnb.view.entity.general;

import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mnb.view.entity.AbstractEntityPanel;

import javax.swing.*;


/**
 * MetabolitePanel – 2011.09.30 <br>
 * Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class GeneralPanel
        extends AbstractEntityPanel {

    private static final Logger LOGGER = Logger.getLogger(GeneralPanel.class);
    private AnnotatedEntity entity;
    private JLabel          formula;
    private JTextField      generic;


    public GeneralPanel() {
        super("General");
    }


    @Override
    public boolean update() {


        // update all fields and labels...

        return super.update();

    }


    @Override
    public boolean setEntity(AnnotatedEntity entity) {
        this.entity = entity;
        return super.setEntity(entity);
    }


    /**
     * Returns the specific information panel
     */
    public JPanel getSynopsis() {

        JPanel panel = new JPanel();


        return panel;

    }


}
