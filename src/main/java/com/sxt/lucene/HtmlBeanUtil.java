package com.sxt.lucene;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

//解析页面，创造HtmlBean
public class HtmlBeanUtil {
    public static HtmlBean parseHtml(File file){

        try {
            Source sc = new Source(file);
            Element title = sc.getFirstElement(HTMLElementName.TITLE);

            if (title == null || title.getTextExtractor() == null){
                return null;
            }

            String content = sc.getTextExtractor().toString();
            HtmlBean hb = new HtmlBean();
            hb.setTitle(title.getTextExtractor().toString());
            hb.setContent(content);
            String path = file.getAbsolutePath();
            hb.setUrl("http://"+path.substring(25));
            return hb;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
