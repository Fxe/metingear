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
package uk.ac.ebi.metingear.search;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import uk.ac.ebi.mdk.domain.entity.AnnotatedEntity;
import uk.ac.ebi.mdk.domain.entity.Reconstruction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * SearchManager – 2011.09.29 <br>
 * Class description
 *
 * @author johnmay
 * @author $Author$ (this version)
 * @version $Rev$ : Last Changed $Date$
 */
public class SearchManager {

    private static final Logger LOGGER = Logger.getLogger(SearchManager.class);
    private Analyzer analyzer;
    private List<AnnotatedEntity> currentResults = new ArrayList<AnnotatedEntity>();
    private SearchableIndex       currentIndex   = null;

    private SearchManager() {
        analyzer = new StandardAnalyzer(Version.LUCENE_34);

    }

    public void setPreviousEntries(List<AnnotatedEntity> previousEntries) {
        this.currentResults = previousEntries;
    }

    public List<AnnotatedEntity> getPreviousEntries() {
        return currentResults;
    }

    private static class SearchManagerHolder {

        private static SearchManager INSTANCE = new SearchManager();
    }

    public static SearchManager getInstance() {
        return SearchManagerHolder.INSTANCE;
    }

    /**
     * Updates the current index. The updating thead is return thus the invoker can choose to wait
     * for indexing to finish using the {@see Thread#wait()} method.
     *
     * @param recon
     *
     * @return
     *
     * @throws org.apache.lucene.index.CorruptIndexException
     *
     * @throws org.apache.lucene.store.LockObtainFailedException
     *
     * @throws java.io.IOException
     */
    public Thread updateCurrentIndex(final Reconstruction recon) throws CorruptIndexException,
            LockObtainFailedException,
            IOException {

        // run index update in separate thread
        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    currentIndex = getIndex(recon);
                } catch (CorruptIndexException ex) {
                    ex.printStackTrace();
                } catch (LockObtainFailedException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        t.setName("SEARCH INDEX THREAD");
        t.start();

        return t;

    }

    public SearchableIndex getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Updates the underlying getIndex
     *
     * @param reconstruction
     *
     * @return
     *
     * @throws org.apache.lucene.index.CorruptIndexException
     *
     * @throws org.apache.lucene.store.LockObtainFailedException
     *
     * @throws java.io.IOException
     */
    public SearchableIndex getIndex(Reconstruction reconstruction) throws CorruptIndexException,
            LockObtainFailedException,
            IOException {

        LOGGER.debug("Invoking search re-index");

        Directory index = new RAMDirectory();
        IndexWriter writer = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_34,
                                                                          analyzer));
        long start = System.currentTimeMillis();
        Map<UUID, AnnotatedEntity> map = DocumentFactory.write(writer,
                                                               reconstruction);
        long end = System.currentTimeMillis();
        LOGGER.debug("Built search index in " + (end - start) + " (ms)");
        writer.close();

        return new SearchableIndex(index, map);

    }

    /**
     * Searches all fields
     *
     * @param query
     *
     * @return
     *
     * @throws org.apache.lucene.queryParser.ParseException
     *
     */
    public Query getQuery(String query) throws ParseException {
        return getQuery(FieldType.getAllFields(), query);
    }

    /**
     * Searches query in specified field
     *
     * @param field
     * @param query
     *
     * @return
     *
     * @throws org.apache.lucene.queryParser.ParseException
     *
     */
    public Query getQuery(FieldType field, String query) throws ParseException {
        return new QueryParser(Version.LUCENE_34, field.getName(), analyzer).parse(query);
    }

    /**
     * Searches query in specified fields
     *
     * @param fields
     * @param query
     */
    public Query getQuery(String[] fields, String query) throws ParseException {
        return new MultiFieldQueryParser(Version.LUCENE_34, fields, analyzer).parse(query);
    }
}
