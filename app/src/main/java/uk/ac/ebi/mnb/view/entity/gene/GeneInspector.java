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
package uk.ac.ebi.mnb.view.entity.gene;

import javax.swing.undo.UndoManager;
import uk.ac.ebi.mnb.view.entity.AbstractEntityInspector;
import org.apache.log4j.Logger;
import uk.ac.ebi.mnb.interfaces.EntityView;

/**
 *          ProteinInspector – 2011.09.28 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class GeneInspector
        extends AbstractEntityInspector {

    private static final Logger LOGGER = Logger.getLogger(GeneInspector.class);

    public GeneInspector() {
        super(new GenePanel());
    }

    @Override
    public void store() {
        super.store();
    }
}
