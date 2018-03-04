package com.sxt.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LuceneController {

    @Autowired
    private CreateIndex index;

    @RequestMapping("/create.do")
    public String createIndex(){
        File file = new File(CreateIndex.indexDir);
        if(file.exists()){
            file.delete();
            file.mkdir();
        }
        index.createIndex();
        return "create.jsp";
    }

    @RequestMapping("/index.do")
    public String search(String keywords, int num, Model model) throws Exception {
        Directory dir = FSDirectory.open(new File(CreateIndex.indexDir));
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);
        Analyzer analyzer = new IKAnalyzer();
        MultiFieldQueryParser mqp = new MultiFieldQueryParser(Version.LUCENE_4_9, new String[]{"title", "content"}, analyzer);
        Query query = mqp.parse(keywords);
        TopDocs search = searcher.search(query, 10* num);
        ScoreDoc[] scoreDocs = search.scoreDocs;
        int count = search.totalHits;
        PageUtil<HtmlBean> page = new PageUtil<HtmlBean>(String.valueOf(num), "10", count);
        List<HtmlBean> ls = new ArrayList<HtmlBean>();

        for (int i = (num-1)*10; i < Math.min(num * 10, count); i++){
            Document document = reader.document(scoreDocs[i].doc);
            SimpleHTMLFormatter sf = new SimpleHTMLFormatter("<font color=\"red\" >","</font>");
            QueryScorer qs = new QueryScorer(query, "title");
            Highlighter highlighter = new Highlighter(sf, qs);
            String title = highlighter.getBestFragment(new IKAnalyzer(), "title", document.get("title"));

            String content = highlighter.getBestFragments(new IKAnalyzer().tokenStream("content", document.get("content")),document.get("content"),3, "...");
            String url = document.get("url");
            HtmlBean hb = new HtmlBean();
            hb.setContent(content);
            hb.setTitle(title);
            hb.setUrl(url);
            ls.add(hb);
        }
        page.setList(ls);
        model.addAttribute("page", page);
        model.addAttribute("keywords", keywords);
        return "index.jsp";
    }
}
