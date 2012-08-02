package net.crfsol.know.core.util;

import net.crfsol.know.core.domain.Resource;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
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
import java.util.List;


public class LuceneUtil {


    @Inject
    private FSDirectory luceneDirectory;

    private IndexSearcher indexSearcher;

    @Inject
    private StandardAnalyzer luceneAnalyzer;

    @Inject
    private QueryParser parser;

    private IndexWriter writer;


    public void startBatch() throws Exception {
        if (writer == null) {
            writer = new IndexWriter(luceneDirectory, new IndexWriterConfig(Version.LUCENE_36, luceneAnalyzer));
        }
    }

    public void finishBatch() throws Exception {
        if (writer != null) {
            writer.close();
        }
    }

    public void indexResources(boolean autoFinishBatch, List<Resource> resources) throws Exception {
        if (resources != null && resources.size() > 0) {
            startBatch();
            for (Resource r : resources) {

            }

            if (autoFinishBatch) {
                finishBatch();
            }
        }


    }

    private Document findDocumentByQuery(String query) {
        try {
            refreshSearcher();
            TopDocs matches = search(query, 1);
            if (matches.totalHits > 0) {
                return getDocByInternalId(matches.scoreDocs[0].doc);
            }
        } catch (Exception e) {
            //TODO: handle
        }
        return null;
    }


    public void createDocument(String id, String content) throws Exception {
        Document d = new Document();
        startBatch();
        d.add(new Field("contents", content, Field.Store.YES, Field.Index.ANALYZED));
        writer.updateDocument(new Term("guid", id), d);
        finishBatch();
    }

    public String getDocumentContentByGUID(String guid) throws Exception {
        refreshSearcher();
        TermQuery query = new TermQuery(new Term("guid", guid));
        TopDocs hits = indexSearcher.search(query, 1);
        if (hits.totalHits > 0) {
            return getDocByInternalId(hits.scoreDocs[0].doc).get("contents");
        } else {
            return null;
        }
    }

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
