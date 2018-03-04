package com.sxt.lucene;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

@Service
public class CreateIndex {
    public static final String indexDir="E:\\CodeLab\\luceneAndSolr\\index";
    public static final String dataDir="E:\\CodeLab\\luceneAndSolr\\www.bjsxt.com\\";
    @Test
    public void createIndex(){
        try {
            Directory dir = FSDirectory.open(new File(indexDir));
            //´´½¨·Ö´ÊÆ÷
            Analyzer analyzer = new IKAnalyzer();
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
            config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            IndexWriter writer = new IndexWriter(dir, config);
            File file = new File(dataDir);

            RAMDirectory ramdir = new RAMDirectory();
            Analyzer analyzer1 = new IKAnalyzer();
            IndexWriterConfig config1 = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
            IndexWriter ramWriter = new IndexWriter(ramdir, config1);

            Collection<File> files = FileUtils.listFiles(file, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);
            int count = 0;
            for(File f : files){
                HtmlBean htmlBean = HtmlBeanUtil.parseHtml(f);
                if (htmlBean == null){
                    continue;
                }
                Document doc = new Document();
                doc.add(new StringField("title", htmlBean.getTitle(), Field.Store.YES));
                doc.add(new TextField("content", htmlBean.getContent(), Field.Store.YES));
                doc.add(new StringField("url", htmlBean.getUrl(),Field.Store.YES));
                ramWriter.addDocument(doc);
                count ++;
                if(count==50){
                    ramWriter.close();
                    writer.addIndexes(ramdir);
                    ramdir = new RAMDirectory();
                    Analyzer analyzer2 = new IKAnalyzer();
                    IndexWriterConfig config2 = new IndexWriterConfig(Version.LUCENE_4_9, analyzer2);
                    ramWriter = new IndexWriter(ramdir, config2);
                    count = 0;
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
