package com.sxt.lucene;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class SearchIndex {
    @Test
    public void search(){
        try {
            Directory dir = FSDirectory.open(new File(CreateIndex.indexDir));
            IndexReader reader = DirectoryReader.open(dir);
            IndexSearcher searcher = new IndexSearcher(reader);
            QueryParser qp = new QueryParser(Version.LUCENE_4_9, "content", new StandardAnalyzer(Version.LUCENE_4_9));
            Query query = qp.parse("java");
            TopDocs search = searcher.search(query, 10);
            ScoreDoc[] scoreDocs = search.scoreDocs;
            for(ScoreDoc sc: scoreDocs){
                int docId = sc.doc;
                Document document = reader.document(docId);
                System.out.println(document.get("filename"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
