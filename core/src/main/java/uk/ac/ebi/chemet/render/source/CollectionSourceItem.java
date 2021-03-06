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
package uk.ac.ebi.chemet.render.source;

import com.explodingpixels.macwidgets.SourceListItem;
import javax.swing.Icon;
import org.apache.log4j.Logger;


/**
 *
 *          CollectionSourceItem 2012.01.30
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 *
 *          Class description
 *
 */
public class CollectionSourceItem extends SourceListItem {

    private static final Logger LOGGER = Logger.getLogger(CollectionSourceItem.class);

    private EntitySubset subset;


    public CollectionSourceItem(EntitySubset subset) {
        super(subset.getName());
        this.subset = subset;
    }


    public CollectionSourceItem(EntitySubset subset, Icon icon) {
        super(subset.getName(), icon);
        this.subset = subset;
    }


    public EntitySubset getSubset() {
        return subset;
    }
}
