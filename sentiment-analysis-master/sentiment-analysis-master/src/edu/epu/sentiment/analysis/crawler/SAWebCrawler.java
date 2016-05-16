package edu.epu.sentiment.analysis.crawler;

import edu.epu.sentiment.analysis.utils.SATime;

import java.util.ArrayList;

/**
 * Created by duong on 3/29/16.
 */
public class SAWebCrawler implements SADelegateCrawler {

    private ArrayList<SABaseCrawler> crawlers;

    public SAWebCrawler() {
        crawlers = new ArrayList<SABaseCrawler>();
        //crawlers.add(new SAAmazonCrawler(SATime.millisecondsInHours(24), new String[]{"comment", "amazon"}));
        crawlers.add(new SAAmazon_CommentCrawler(SATime.millisecondsInHours(24), new String[]{"comment", "Comment_amazon"}));
      //  crawlers.add(new SAVietBaoVnCrawler(SATime.millisecondsInHours(24), new String[]{"goldprice", "vietbaovn"}));
       // crawlers.add(new SAGiaVangNetCrawler(SATime.millisecondsInHours(24), new String[]{"goldprice", "giavangnet"}));
       // crawlers.add(new SATyGiaVangVnCrawler(SATime.millisecondsInHours(24), new String[]{"goldprice", "tygiavangvn"}));
    }

    @Override
    public void execute() {
        for (SABaseCrawler crawler : crawlers) {
            crawler.execute();
        }
    }
}