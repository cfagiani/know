package net.crfsol.know.core.util;

import net.crfsol.know.core.domain.Resource;
import net.crfsol.know.core.domain.ResourceType;
import net.crfsol.know.core.domain.Tag;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Fieldable;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import javax.inject.Inject;
import java.util.*;


/**
 * Utility that wraps the functionality of Lucene for both Indexing and Searching.
 * <p/>
 * TODO: refactor this class to make more efficient use of the Lucene index
 * TODO: this class may need to be made threadsafe if threads are going to be used in the Engine
 * TODO: fix the exception handling
 *
 * @author Christopher Fagiani
 */
public class LuceneUtil {

    private static final int MAX_HITS = 100;
    private static final String CONTENT_FIELD = "contents";
    private static final String GUID_FIELD = "guid";
    private static final String LOC_FIELD = "location";
    private static final String TITLE_FIELD = "title";
    private static final String TYPE_FIELD = "type";

    @Inject
    private FSDirectory luceneDirectory;

    private IndexSearcher indexSearcher;

    @Inject
    private StandardAnalyzer luceneAnalyzer;

    @Inject
    private QueryParser parser;

    private IndexWriter writer;


    /**
     * prepares to write a batch of documents to the index by initializing a new IndexWriter
     *
     * @throws Exception
     */
    public void startBatch() throws Exception {
        if (writer == null) {
            writer = new IndexWriter(luceneDirectory, new IndexWriterConfig(Version.LUCENE_36, luceneAnalyzer));
        }
    }

    /**
     * finishes a batch and flushes the index by closing the IndexWriter.
     *
     * @throws Exception
     */
    public void finishBatch() throws Exception {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }

    /**
     * indexes a batch of Resources. All Resources will be converted to Documents and written to the index. This method
     * will call startBatch automatically before writing any documents. If autoFinishBatch is true, finishBatch will be called
     * after all Resources in the collection have been added to the index.
     *
     * @param autoFinishBatch
     * @param resources
     * @throws Exception
     */
    public void indexResources(boolean autoFinishBatch, List<Resource> resources) throws Exception {
        if (resources != null && resources.size() > 0) {
            startBatch();
            for (Resource r : resources) {
                indexResource(r);
            }

            if (autoFinishBatch) {
                finishBatch();
            }
        }
    }

    /**
     * Converts a Resource to a Document and indexes it. Indexing is performed via the updateDocument method, passing in
     * a TermQuery on the location as an argument (therefore all Resources in the index must have a unique Location).
     *
     * @param r
     * @throws Exception
     */
    public void indexResource(Resource r) throws Exception {
        Document doc = new Document();
        if (r.getTextContent() != null) {
            doc.add(new Field(CONTENT_FIELD, r.getName() + "\n" + r.getTextContent(), Field.Store.YES, Field.Index.ANALYZED));
        } else {
            doc.add(new Field(CONTENT_FIELD, r.getName(), Field.Store.YES, Field.Index.ANALYZED));
        }
        doc.add(new Field(LOC_FIELD, r.getLocation(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
        doc.add(new Field("lastIndexed", new Date().getTime() + "", Field.Store.YES, Field.Index.NO));
        doc.add(new Field(TITLE_FIELD, r.getName(), Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field(TYPE_FIELD, r.getType().getCode(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));

        if (r.getTags() != null) {
            for (Tag t : r.getTags()) {
                doc.add(new Field("tag", t.getLabel(), Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
            }
        }
        writer.updateDocument(new Term(LOC_FIELD, r.getLocation()), doc);

    }

    /**
     * searches for resources using the query passed in. This method limits the search results to MAX_HITS.
     *
     * @param query
     * @return
     */
    public List<Resource> resourceSearch(String query) {
        List<Resource> results = new ArrayList<Resource>();
        try {
            refreshSearcher();
            TopDocs matches = search(query, MAX_HITS);

            if (matches.totalHits > 0) {
                for (int i = 0; i < matches.totalHits; i++) {
                    results.add(convertDocToResource(matches.scoreDocs[i].doc));
                }
            }
        } catch (Exception e) {
            //TODO: handle
        }
        return results;
    }

    /**
     * utility method to return a fully-hydrated Resource given a documentId (internal Lucene document id). The id will
     * be fed to the indexReader to get the actual document and then this document will be used to populate the Resource.
     *
     * @param docId
     * @return
     */
    private Resource convertDocToResource(int docId) {
        Document doc = getDocByInternalId(docId);
        Resource r = new Resource();
        if (doc.getFieldable(TITLE_FIELD) != null) {
            r.setName(doc.getFieldable(TITLE_FIELD).stringValue());
        }
        if (doc.getFieldable(CONTENT_FIELD) != null) {
            r.setTextContent(doc.getFieldable(CONTENT_FIELD).stringValue());
        }
        Fieldable[] tagFields = doc.getFieldables("tag");
        if (tagFields != null && tagFields.length > 0) {
            Set<Tag> tags = new HashSet<Tag>();
            for (Fieldable tagField : tagFields) {
                tags.add(new Tag(tagField.stringValue()));
            }
            r.setTags(tags);
        }
        r.setLocation(doc.getFieldable(LOC_FIELD).stringValue());
        r.setType(new ResourceType(doc.getFieldable(TYPE_FIELD).stringValue()));
        return r;

    }

    /**
     * creates a document that has a GUID field set to the id passed in and contains the content String as its contents field.
     * <p/>
     * TODO: decide whether to collapse this into the other indexing method
     *
     * @param id
     * @param content
     * @throws Exception
     */
    public synchronized void createDocument(String id, String content) throws Exception {
        Document d = new Document();
        startBatch();
        d.add(new Field(GUID_FIELD, id, Field.Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));
        d.add(new Field(CONTENT_FIELD, content, Field.Store.YES, Field.Index.ANALYZED));
        writer.updateDocument(new Term(GUID_FIELD, id), d);
        finishBatch();
    }

    /**
     * returns the Contents of a single document that matches the GUID passed in. If no match is found, this method returns null.
     *
     * @param guid
     * @return
     * @throws Exception
     */
    public String getDocumentContentByGUID(String guid) throws Exception {
        refreshSearcher();
        TermQuery query = new TermQuery(new Term(GUID_FIELD, guid));
        TopDocs hits = indexSearcher.search(query, 1);
        if (hits.totalHits > 0) {
            return getDocByInternalId(hits.scoreDocs[0].doc).get(CONTENT_FIELD);
        } else {
            return null;
        }
    }

    /**
     * returns a fully-hydrated Resource from the index if it matches the location passed in. If no match is found, this method returns null.
     *
     * @param location
     * @return
     * @throws Exception
     */
    public Resource getResourceByLocation(String location) throws Exception {
        refreshSearcher();
        TermQuery query = new TermQuery(new Term(LOC_FIELD, location));
        TopDocs hits = indexSearcher.search(query, 1);
        if (hits.totalHits > 0) {
            return convertDocToResource(hits.scoreDocs[0].doc);
        } else {
            return null;
        }
    }

    /**
     * returns a full Document given an internal lucene DocumentId
     *
     * @param docId
     * @return
     */
    private Document getDocByInternalId(int docId) {
        try {
            refreshSearcher();
            return indexSearcher.doc(docId);
        } catch (Exception e) {
            //TODO: handle
            e.printStackTrace();
        }
        return null;
    }


    /**
     * peforms a search against an indexSearcher and returns the TopDocs structure with the results.
     *
     * @param queryString
     * @param maxHits
     * @return
     */
    private TopDocs search(String queryString, int maxHits) {
        try {
            refreshSearcher();
            Query query = parser.parse(queryString);
            return indexSearcher.search(query, null, maxHits);
        } catch (Exception e) {
            //todo: handle
            e.printStackTrace();
        }
        return null;
    }


    /**
     * this method will return the current indexSearcher or, if the indexSearcher is out of date (i.e. the index has been updated), it
     * will close the old searcher and open a new one, installing it as a class member (overwriting the out of date instance).
     *
     * @throws Exception
     */
    private void refreshSearcher() throws Exception {
        if (indexSearcher != null && !indexSearcher.getIndexReader().isCurrent()) {
            indexSearcher.getIndexReader().close();
            indexSearcher.close();
            indexSearcher = null;
        }
        if (indexSearcher == null) {
            indexSearcher = new IndexSearcher(IndexReader.open(luceneDirectory));
        }
    }


}
