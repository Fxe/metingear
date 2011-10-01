
/**
 * BorderlessScrollPane.java
 *
 * 2011.09.07
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
package uk.ac.ebi.mnb.view.entity;

import com.jgoodies.forms.factories.Borders;
import java.awt.Component;
import javax.swing.JScrollPane;
import org.apache.log4j.Logger;


/**
 *          BorderlessScrollPane – 2011.09.07 <br>
 *          Wraps JScrollPane with a constructor that uses an empty border
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class BorderlessScrollPane extends JScrollPane {

    private static final Logger LOGGER = Logger.getLogger(BorderlessScrollPane.class);


    public BorderlessScrollPane(Component view) {
        super(view);
        setBorder(Borders.EMPTY_BORDER);
        setViewportBorder(Borders.EMPTY_BORDER);
    }


}

