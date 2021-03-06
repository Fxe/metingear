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

package uk.ac.ebi.metingear.edit.entity;

import uk.ac.ebi.mdk.domain.entity.Gene;
import uk.ac.ebi.mdk.domain.entity.GeneProduct;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.collection.Chromosome;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import java.util.Collection;
import java.util.HashSet;

/**
 * An undoable edit which removed
 *
 * @author John May
 */
public class RemoveGeneEdit extends AbstractUndoableEdit {

    private final Collection<GeneProduct> products;
    private final Chromosome chromosome;
    private final Gene gene;
    private final Reconstruction reconstruction;

    public RemoveGeneEdit(Reconstruction reconstruction, Gene gene) {
        this.chromosome = gene.chromosome();
        this.products = new HashSet<GeneProduct>(reconstruction.productsOf(gene));
        this.gene = gene;
        this.reconstruction = reconstruction;
    }

    @Override
    public void undo() throws CannotUndoException {
        super.undo();

        // add back product references
        for (GeneProduct product : products) {
            reconstruction.associate(gene, product);
        }
        // add back to chromosome
        if(this.chromosome != null)
            this.chromosome.add(gene);
    }

    @Override
    public void redo() throws CannotUndoException {
        super.redo();
        // remove product references
        for (GeneProduct product : products) {
            reconstruction.dissociate(gene, product);
        }

        // remove from chromosome
        if(this.chromosome != null)
            this.chromosome.remove(gene);
    }

}
