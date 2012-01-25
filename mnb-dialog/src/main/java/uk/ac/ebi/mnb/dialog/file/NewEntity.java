/**
 * NewEntity.java
 *
 * 2011.10.04
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
package uk.ac.ebi.mnb.dialog.file;

import java.util.*;
import javax.swing.event.UndoableEditListener;
import uk.ac.ebi.caf.report.ReportManager;
import uk.ac.ebi.mnb.interfaces.SelectionController;
import uk.ac.ebi.mnb.interfaces.TargetedUpdate;
import uk.ac.ebi.mnb.interfaces.Updatable;
import uk.ac.ebi.mnb.view.*;
import uk.ac.ebi.resource.IdentifierFactory;

import javax.swing.*;

import org.apache.log4j.Logger;

import com.jgoodies.forms.layout.*;
import uk.ac.ebi.interfaces.identifiers.Identifier;
import uk.ac.ebi.mnb.core.ControllerDialog;
import uk.ac.ebi.caf.component.factory.FieldFactory;
import uk.ac.ebi.caf.component.factory.LabelFactory;

/**
 * @name    NewEntity - 2011.10.04 <br>
 *          Base class for new entities. Provides operations on basic information setting (i.e. Abbreviation, Name
 *          and Accession).
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class NewEntity extends ControllerDialog {

    private static final Logger LOGGER = Logger.getLogger(NewEntity.class);
    private JComboBox type;
    private JTextField accession = FieldFactory.newField(6);
    private JTextField name = FieldFactory.newField(20);
    private JTextField abbreviation = FieldFactory.newField(4);
    private CellConstraints cc = new CellConstraints();
    private Updatable updateable;
    private static final Map<String, Byte> nameIndexMap = new HashMap();

    /**
     * Provide the frame to attach the dialog to and the default identifier type
     * BasicChemicalIdentifier, BasicProteinIdentifier BasicReactionIdentifier
     * @param frame
     */
    public NewEntity(JFrame frame,
                     TargetedUpdate updater,
                     ReportManager messages,
                     SelectionController controller,
                     UndoableEditListener undoableEdits) {
        super(frame, updater, messages, controller, undoableEdits, "SaveDialog");

        for (Identifier id : IdentifierFactory.getInstance().getSupportedIdentifiers()) {
            nameIndexMap.put(id.getShortDescription(), id.getIndex());
        }
        type = new MComboBox(nameIndexMap.keySet());

        this.updateable = updater;

        setDefaultLayout();


    }

    @Override
    public JPanel getOptions() {

        JPanel panel = super.getOptions();

        panel.setLayout(new FormLayout("p, 4dlu, p, 4dlu, p, 4dlu, p",
                                       "p, 4dlu, p"));

        panel.add(type, cc.xy(1, 1));
        panel.add(accession, cc.xy(3, 1));

        panel.add(LabelFactory.newFormLabel("Abbreviation:",
                                            "A short 2-5 character abbreviation of the new entity"),
                  cc.xy(6, 1));
        panel.add(abbreviation, cc.xy(7, 1));

        panel.add(LabelFactory.newFormLabel("Name:", "An offical or trivial name the new entity"), cc.xy(1, 3));
        panel.add(name, cc.xyw(3, 3, 5));


        return panel;

    }

    /**
     * Returns the value of the name field
     * @return
     */
    public String getName() {
        return name.getText().trim();
    }

    /**
     * Returns the value of the name field
     * @return
     */
    public String getAbbreviation() {
        return abbreviation.getText().trim();
    }

    /**
     * Returns the value of the name field
     * @return
     */
    public Identifier getIdentifier() {
        Identifier id = IdentifierFactory.getInstance().ofIndex(nameIndexMap.get(type.getSelectedItem()));
        id.setAccession(accession.getText().trim());
        return id;
    }

    @Override
    public boolean update() {
        return updateable.update();
    }
}
