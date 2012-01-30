/**
 * EntityTableModel.java
 *
 * 2011.09.06
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

import java.util.*;

import uk.ac.ebi.chemet.render.source.*;
import uk.ac.ebi.core.Reconstruction;
import uk.ac.ebi.core.ReconstructionManager;
import uk.ac.ebi.interfaces.AnnotatedEntity;

import javax.swing.table.AbstractTableModel;

import javax.swing.undo.UndoManager;
import org.apache.log4j.Logger;
import uk.ac.ebi.edit.entity.AbbreviationSetter;
import uk.ac.ebi.edit.entity.NameSetter;
import uk.ac.ebi.interfaces.entities.EntityCollection;
import uk.ac.ebi.mnb.main.MainView;

/**
 *          EntityTableModel – 2011.09.06 <br>
 *          Class description
 * @version $Rev$ : Last Changed $Date$
 * @author  johnmay
 * @author  $Author$ (this version)
 */
public abstract class AbstractEntityTableModel
        extends AbstractTableModel {

    private static final Logger LOGGER = Logger.getLogger(AbstractEntityTableModel.class);
    private Reconstruction currentReconstruction;
    private ReconstructionManager pm = ReconstructionManager.getInstance();
    private List<ColumnDescriptor> columnDescriptors = new ArrayList();
    private List<? extends AnnotatedEntity> components = new ArrayList<AnnotatedEntity>();
    private static List<ColumnDescriptor> defaultColumns = new ArrayList(
            Arrays.asList(new ColumnDescriptor("Accession", String.class,
                                               DataType.BASIC, String.class, false),
                          new ColumnDescriptor("Abbreviation", String.class,
                                               DataType.BASIC, String.class, new AbbreviationSetter()),
                          new ColumnDescriptor("Name", String.class, DataType.BASIC,
                                               String.class, new NameSetter())));
    private Map<String, Accessor> accessMap = new HashMap();

    public AbstractEntityTableModel() {
        this(defaultColumns);
        accessMap.put("Name", new NameAccessor());
        accessMap.put("Abbreviation", new AbbreviationAccessor());
        accessMap.put("Accession", new AccessionAccessor());
    }

    public AbstractEntityTableModel(List<ColumnDescriptor> columnDescriptors) {
        this.columnDescriptors.addAll(columnDescriptors);
        accessMap.put("Name", new NameAccessor());
        accessMap.put("Abbreviation", new AbbreviationAccessor());
        accessMap.put("Accession", new AccessionAccessor());
    }

    /**
     * Returns the default columns Accession, Abbreviation and Name
     */
    public static List<ColumnDescriptor> getDefaultColumns() {
        return defaultColumns;
    }

    /**
     * Returns the component and given index
     * @param index
     * @return
     */
    public AnnotatedEntity getEntity(int index) {
        return components.get(index);
    }
    private Object[][] data;

    /**
     *
     * Updates the underlying table-model
     *
     */
    public boolean update() {
        loadComponents();
        data = new Object[components.size()][getColumnCount()];
        long start = System.currentTimeMillis();
        for (int i = 0; i < components.size(); i++) {
            for (int j = 0; j < getColumnCount(); j++) {
                Object newValue = getValue(components.get(i), j);
                if (!newValue.equals(data[i][j])) {
                    data[i][j] = newValue;
                }
            }
        }
        long end = System.currentTimeMillis();
        LOGGER.info("Loaded table data: " + getClass() + " : " + (end - start) + " ms ");
        fireTableDataChanged();
        return true;
    }

    public boolean update(AnnotatedEntity entity) {
        int index = indexOf(entity);
        if (index == -1) {
            LOGGER.error("Skiping update on item: " + entity);
        }

        for (int j = 0; j < getColumnCount(); j++) {
            Object newValue = getValue(components.get(index), j);
            if (!newValue.equals(data[index][j])) {
                // do this and providing the equals method isn't to tasking
                // there is little effect on speed and no user interuption
                // (i.e. an object won't change that a user is updating)
                data[index][j] = newValue;
            }
        }

        fireTableRowsUpdated(index, index);
        return true;
    }

    /**
     * Updates only a subset of table data
     */
    public boolean update(EntityCollection selection) {

        long start = System.currentTimeMillis();

        for (AnnotatedEntity entity : selection.getEntities()) {
            update(entity);
        }

        long end = System.currentTimeMillis();

        LOGGER.info((end - start) + " (ms) ");

        return true;

    }

    @Override
    public boolean isCellEditable(int rowIndex,
                                  int columnIndex) {
        return columnDescriptors.get(columnIndex).isEditable();
    }

    /**
     * Method is called on update before cells are copied over to data[][]
     */
    public abstract void loadComponents();

    public void setEntities(List<? extends AnnotatedEntity> components) {
        this.components = components;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnDescriptors.get(columnIndex).getDataClass();
    }

    @Override
    public String getColumnName(int column) {
        return columnDescriptors.get(column).getName();
    }

    public int getColumnCount() {
        return columnDescriptors.size();
    }

    public boolean addColumns(List<ColumnDescriptor> descriptors) {
        return columnDescriptors.addAll(descriptors);
    }

    public boolean addColumn(ColumnDescriptor... descriptors) {
        return columnDescriptors.addAll(Arrays.asList(descriptors));
    }

    public boolean addColumn(ColumnDescriptor descriptor) {
        return columnDescriptors.add(descriptor);
    }

    public DataType getAccessType(Integer column) {
        return columnDescriptors.get(column).getType();
    }

    public int getRowCount() {
        return components.size();
    }

    @Override
    public void setValueAt(Object value,
                           int rowIndex,
                           int columnIndex) {
        AnnotatedEntity entity = components.get(rowIndex);
        ColumnDescriptor desc = columnDescriptors.get(columnIndex);

        System.out.println(desc.getName() + ":" + desc.hasSetter());

        if (desc.hasSetter()) {
            UndoManager undoManager = MainView.getInstance().getUndoManager();
            undoManager.addEdit(desc.getUndoableEdit(entity, value));
            desc.set(entity, value);
            update();
        }

    }

    @Override
    public Object getValueAt(int rowIndex,
                             int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public Object getValue(AnnotatedEntity entity,
                           Integer columnIndex) {

        DataType type = getAccessType(columnIndex);
        if (type == DataType.FIXED) {
            return getFixedType(entity, getColumnName(columnIndex));
        } else if (type == DataType.BASIC) {
            return getBasicInfo(entity, getColumnName(columnIndex));
        } else if (type == DataType.ANNOTATION) {
            return entity.getAnnotationsExtending(columnDescriptors.get(columnIndex).getAccessClass());
        } else if (type == DataType.OBSERVATION) {
            return null;
//            return entity.getObservationCollection().get(
//                    columnDescriptors.get(columnIndex).getAccessClass());
        }

        // will never happen... i hope :-)
        LOGGER.error("Unknown ColumnAccessType?");

        return "";

    }

    public abstract Object getFixedType(AnnotatedEntity component,
                                        String name);

    public Integer indexOf(AnnotatedEntity component) {
        return components.indexOf(component);
    }

    private Object getBasicInfo(AnnotatedEntity entity,
                                String name) {

        Accessor accessor = accessMap.get(name);

        if (accessor != null) {
            return accessor.access(entity);
        }
        // maybe do this when os x has java 1.7
//        switch (name) {
//            case "Name":
//                return entity.getName();
//            case "Abbreviation":
//                return entity.getName();
//            case "Accession":
//                return entity.getName();
//            default:
//                return "NA";
//        }
//


        return "NA";

    }

    public void clear() {
        components.clear();
        update();
    }
}
