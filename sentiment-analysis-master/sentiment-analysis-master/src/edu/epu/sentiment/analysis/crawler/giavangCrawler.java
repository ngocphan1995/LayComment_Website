package edu.epu.sentiment.analysis.crawler;
import edu.epu.sentiment.analysis.utils.SALog;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
/**
 * Created by NgocKute on 08/04/2016.
 */
public class giavangCrawler extends SABaseCrawler {

    public giavangCrawler(long sleepTime, String[] folders) {
        super(sleepTime, folders);
        groups.add(new SAGroupCrawler("http://tygiavang.vn/gia-vang-trong-nuoc/", "http://tygiavang.vn/gia-vang-trong-nuoc/page/%s/", 2, 259, 1));
        groups.add(new SAGroupCrawler("http://tygiavang.vn/gia-vang-the-gioi/", "http://tygiavang.vn/gia-vang-the-gioi/page/%s/", 2, 322, 1));
    }

    @Override
    public ArrayList<String> getChildUrls(String parentUrl) {
        return null;
    }

    @Override
    public SADocumentCrawler getDocFromUrl(String url) {
        return null;
    }
}
