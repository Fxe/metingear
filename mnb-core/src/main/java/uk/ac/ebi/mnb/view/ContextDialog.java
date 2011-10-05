/**
 * ComponentActionDialog.java
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
package uk.ac.ebi.mnb.view;

import java.util.Collection;
import javax.swing.JFrame;
import uk.ac.ebi.core.AnnotatedEntity;
import uk.ac.ebi.mnb.interfaces.SelectionController;
import uk.ac.ebi.mnb.interfaces.ViewController;

/**
 * @name    ComponentActionDialog - 2011.10.03 <br>
 *          A DropdownDialog that acts on the current selection
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class ContextDialog extends DropdownDialog implements SelectionController {

    private ViewController controller;

    public ContextDialog(JFrame frame, ViewController controller, String dialogName) {
        super(frame, dialogName);
        this.controller = controller;
    }

    /**
     * @inheritDoc
     */
    public Collection<AnnotatedEntity> getSelection() {
        return controller.getSelection();
    }

    /**
     * @inheritDoc
     */
    public boolean setSelection(AnnotatedEntity entity) {
        return controller.setSelection(entity);
    }

    /**
     * @inheritDoc
     */
    public boolean setSelection(Collection<? extends AnnotatedEntity> entities) {
        return this.controller.setSelection(entities);
    }

    /**
     * @inheritDoc
     */
    public AnnotatedEntity getSelectedEntity() {
        return getSelectedEntity();
    }

    public ViewController getController() {
        return controller;
    }
}