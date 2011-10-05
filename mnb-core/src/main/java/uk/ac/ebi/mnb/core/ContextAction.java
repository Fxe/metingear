/**
 * SelectionAction.java
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
package uk.ac.ebi.mnb.core;

import java.util.Collection;
import uk.ac.ebi.core.AnnotatedEntity;
import uk.ac.ebi.mnb.interfaces.MainController;
import uk.ac.ebi.mnb.interfaces.Message;

/**
 * @name    SelectionAction - 2011.10.03 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class ContextAction extends GeneralAction {

    private MainController controller;

    public ContextAction(String command, MainController controller) {
        super(command);
        this.controller = controller;
    }

    public Collection<AnnotatedEntity> getSelection() {
        return controller.getViewController().getSelection();
    }

    public boolean setSelection(Collection<? extends AnnotatedEntity> entities) {
        return controller.getViewController().setSelection(entities);
    }

    public void addMessage(Message mesg){
        controller.getMessageManager().addMessage(mesg);
    }
}