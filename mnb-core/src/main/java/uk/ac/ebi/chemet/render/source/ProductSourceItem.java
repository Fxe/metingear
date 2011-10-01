
/**
 * MetaboliteSourceItem.java
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
package uk.ac.ebi.chemet.render.source;

import com.explodingpixels.macwidgets.SourceListItem;
import com.explodingpixels.macwidgets.SourceListModel;
import org.apache.log4j.Logger;
import uk.ac.ebi.core.AnnotatedEntity;
import uk.ac.ebi.mnb.core.ApplicationPreferences;


/**
 *          MetaboliteSourceItem – 2011.09.30 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public class ProductSourceItem
  extends EntitySourceItem {

    private static final Logger LOGGER = Logger.getLogger(ProductSourceItem.class);


    public ProductSourceItem(AnnotatedEntity entity, Object container) {
        super(entity, container);
    }


    /**
     * @inheritDoc
     */
    @Override
    public void update() {
        ApplicationPreferences pref = ApplicationPreferences.getInstance();
        String type = pref.getProperty(ApplicationPreferences.VIEW_SOURCE_PRODUCT);
        super.update(type);
    }


    @Override
    public void remove(SourceListModel model) {
        model.removeItemFromItem(this, (SourceListItem) super.container);
    }


}

