package edu.epu.sentiment.analysis.crawler;

import edu.epu.sentiment.analysis.utils.SALog;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by duong on 3/29/16.
 */
public class SATyGiaVangVnCrawler extends SABaseCrawler {

    public SATyGiaVangVnCrawler(long sleepTime, String[] folders) {
        super(sleepTime, folders);
        groups.add(new SAGroupCrawler("http://tygiavang.vn/gia-vang-trong-nuoc/", "http://tygiavang.vn/gia-vang-trong-nuoc/page/%s/", 2, 259, 1));
        groups.add(new SAGroupCrawler("http://tygiavang.vn/gia-vang-the-gioi/", "http://tygiavang.vn/gia-vang-the-gioi/page/%s/", 2, 322, 1));
    }

    @Override
    public ArrayList<String> getChildUrls(String parentUrl) {
        ArrayList<String> urls = new ArrayList<String>();
        try {
            Document document = SADocumentCrawler.getDocumentFromUrl(parentUrl);
            Elements elements = document.select("h2");
            for (Element element : elements) {
                Elements tagElements = element.getElementsByTag("a");
                if (tagElements != null) {
                    String href = tagElements.attr("href").trim();
                    urls.add(href);
                    SALog.log("GET", href);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return urls;
    }

    @Override
    public SADocumentCrawler getDocFromUrl(String url) {
        SADocumentCrawler documentCrawler = new SADocumentCrawler(url);
        try {
            Document document = SADocumentCrawler.getDocumentFromUrl(url);
            Element bodyElement = document.body();
            //--------------------------------------------------------------------------------------------------------//
            Elements titleElements = bodyElement.select("h1.posttitle");
            String titleString = titleElements.text().trim();
            documentCrawler.setTitle(titleString);
            //--------------------------------------------------------------------------------------------------------//
            Elements bodyElements = bodyElement.select("div.entry");
            if (bodyElements != null) {
                Elements tagElements = bodyElements.select("div.ptags");
                if (tagElements != null) {
                    Elements aElements = tagElements.select("a");
                    StringBuffer tags = new StringBuffer();
                    for (Element aElement : aElements) {
                        tags.append(aElement.text().trim());
                        tags.append(",");
                    }
                    if (tags.length() > 0) {
                        tags.deleteCharAt(tags.length() - 1);
                    }
                    documentCrawler.setTags(tags.toString());
                }
                Elements quoteElements = bodyElements.select("div.qoute");
                if (quoteElements != null) {
                    quoteElements.remove();
                }
                Elements pElements = bodyElements.select("p");
                String bodyString = pElements.text().trim();
                documentCrawler.setBody(bodyString);
            }
            //--------------------------------------------------------------------------------------------------------//
            Elements dateTimeElements = bodyElement.select("div#datemeta_l");
            if (dateTimeElements != null) {
                //Sat, Apr 2nd, 2016 -> 02-04-2016
                String[] dateTimeStrings = dateTimeElements.text().split(",");
                String dateTimeString = dateTimeStrings[1].trim() + " " + dateTimeStrings[2].trim();
                DateFormat sourceDateFormat = new SimpleDateFormat("MMM dd yyyy", Locale.ENGLISH);
                DateFormat targetDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                Date date = sourceDateFormat.parse(dateTimeString.replaceAll("st|nd|rd|th", ""));
                dateTimeString = targetDateFormat.format(date).toString();
                documentCrawler.setDateTime(dateTimeString);
            }
            //--------------------------------------------------------------------------------------------------------//
            Elements authorElements = bodyElements.select("strong");
            for (Element authorElement : authorElements) {
                if (authorElement.text().contains("Nguồn")) {
                    String authorString = authorElement.text().trim().split("/")[0].split(" ")[1].trim();
                    documentCrawler.setAuthor(authorString);
                }
            }
            //--------------------------------------------------------------------------------------------------------//
            documentCrawler.printDocument();
            documentCrawler.writeDocument(directory);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return documentCrawler;
    }

}
