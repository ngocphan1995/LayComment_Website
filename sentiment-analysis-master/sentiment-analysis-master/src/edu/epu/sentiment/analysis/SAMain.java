package edu.epu.sentiment.analysis;

import edu.epu.sentiment.analysis.crawler.SAWebCrawler;
import edu.epu.sentiment.analysis.utils.SADate;
import edu.epu.sentiment.analysis.utils.SALog;

/**
 * Created by duong on 3/27/16.
 */
public class SAMain {

    public static void main(String[] args) {
        SALog.prln("Sentiment Analysis");
        SADate.getInstance();
        SAWebCrawler crawler = new SAWebCrawler();
        crawler.execute();
    }

}
