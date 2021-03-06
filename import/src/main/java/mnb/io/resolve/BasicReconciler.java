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

package mnb.io.resolve;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import mnb.io.tabular.preparse.PreparsedEntry;
import mnb.io.tabular.preparse.PreparsedMetabolite;
import mnb.io.tabular.type.EntityColumn;
import org.apache.log4j.Logger;
import uk.ac.ebi.mdk.domain.annotation.MolecularFormula;
import uk.ac.ebi.mdk.domain.annotation.Synonym;
import uk.ac.ebi.mdk.domain.annotation.crossreference.KEGGCrossReference;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.entity.DefaultEntityFactory;
import uk.ac.ebi.mdk.domain.entity.Metabolite;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;
import uk.ac.ebi.mdk.domain.entity.collection.DefaultReconstructionManager;
import uk.ac.ebi.mdk.domain.identifier.KEGGCompoundIdentifier;
import uk.ac.ebi.mdk.domain.identifier.basic.BasicChemicalIdentifier;

import java.util.Collection;

import static mnb.io.tabular.type.EntityColumn.FORMULA;


/**
 * AutomatedReconciler – 2011.09.23 <br> Automated reconciler assign description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date: 2011-11-19 10:15:40 +0000 (Sat, 19
 *          Nov 2011) $
 */
public class BasicReconciler
        implements EntryReconciler {

    private static final Logger LOGGER = Logger.getLogger(BasicReconciler.class);

    private Reconstruction recon;

    private Multimap<String, Metabolite> nameMap;


    public BasicReconciler() {
        recon = DefaultReconstructionManager.getInstance().active();
        nameMap = HashMultimap.create();
        if (recon != null && !recon.metabolome().isEmpty()) {
            for (Metabolite m : recon.metabolome()) {
                nameMap.put(m.getName(), m);
            }
        }
    }


    /**
     * @param entry
     *
     * @return @inheritDoc
     */
    public AnnotatedEntity resolve(PreparsedEntry entry) {
        if (entry instanceof PreparsedMetabolite) {
            return resolve((PreparsedMetabolite) entry);
        }
        return null;
    }

    private static int ticker = 0;


    /**
     * Automatically resolves
     *
     * @param entry
     *
     * @return
     */
    public Metabolite resolve(PreparsedMetabolite entry) {

        String[] names = entry.getNames();

        String name = names.length > 0 ? names[0] : "Unnamed metabolite";

        if (nameMap.containsKey(name)) {
            Collection<Metabolite> candidates = nameMap.get(name);
            if (candidates.size() == 1) {
                return candidates.iterator().next();
            } else {
                LOGGER.error("Duplicate metabolites with same name!");
            }
        }

        Metabolite metabolite = DefaultEntityFactory.getInstance().ofClass(Metabolite.class,
                                                                           BasicChemicalIdentifier.nextIdentifier(),
                                                                           name,
                                                                           entry.getAbbreviation());

        for (int i = 1; i < names.length; i++) {
            metabolite.addAnnotation(new Synonym(names[i]));
        }

        // add synonyms if they were provided
        if (entry.hasValue(EntityColumn.SYNONYMS)) {
            for (String synonym : entry.getValues(EntityColumn.SYNONYMS)) {
                metabolite.addAnnotation(new Synonym(synonym));
            }
        }

        // molecula formula
        if (entry.hasValue(FORMULA)) {
            metabolite.addAnnotation(new MolecularFormula(entry.getFormula()));
        }

        if (entry.getCharge() != null) {
            try {
                metabolite.setCharge(Double.parseDouble(entry.getCharge()));
            } catch (NumberFormatException ex) {
                LOGGER.error("Invalid format for charge: " + entry.getCharge());
            }
        }

        // adds the kegg xref
        for (String xref : entry.getKEGGXREFs()) {
            KEGGCompoundIdentifier keggId = new KEGGCompoundIdentifier(xref);
            metabolite.addAnnotation(new KEGGCrossReference(keggId));
        }


        return metabolite;

    }
}
