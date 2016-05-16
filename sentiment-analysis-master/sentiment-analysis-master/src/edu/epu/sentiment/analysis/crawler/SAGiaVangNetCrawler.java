package edu.epu.sentiment.analysis.crawler;

import edu.epu.sentiment.analysis.utils.SALog;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by duong on 3/29/16.
 */
public class SAGiaVangNetCrawler extends SABaseCrawler {

    public SAGiaVangNetCrawler(long sleepTime, String[] folders) {
        super(sleepTime, folders);
        groups.add(new SAGroupCrawler("http://www.giavang.net/category/vang-the-gioi/", "http://www.giavang.net/category/vang-the-gioi/page/%s/", 2, 198, 1));
        groups.add(new SAGroupCrawler("http://www.giavang.net/category/vang-trong-nuoc-2/", "http://www.giavang.net/category/vang-trong-nuoc-2/page/%s/", 2, 87, 1));
        groups.add(new SAGroupCrawler("http://www.giavang.net/category/phan-tich-vang/", "http://www.giavang.net/category/phan-tich-vang/page/%s/", 2, 232, 1));
    }

    @Override
    public ArrayList<String> getChildUrls(String parentUrl) {
        ArrayList<String> urls = new ArrayList<String>();
        try {
            Document document = SADocumentCrawler.getDocumentFromUrl(parentUrl);
            Elements elements = document.select("div.category3-text");
            if (elements != null) {
                for (Element element : elements) {
                    Elements tagElements = element.getElementsByTag("a");
                    if (tagElements != null) {
                        String href = tagElements.attr("href");
                        urls.add(href);
                        SALog.log("GET", href);
                    }
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
            Elements titleElements = bodyElement.select("h1.headline");
            String titleString = titleElements.text().trim();
            documentCrawler.setTitle(titleString);
            //--------------------------------------------------------------------------------------------------------//
            Elements bodyElements = bodyElement.select("div#content-area");
            if (bodyElements != null) {
                String bodyString = bodyElements.text().trim();
                documentCrawler.setBody(bodyString);
            }
            Elements dateTimeElements = bodyElement.select("div#post-info-left");
            if (dateTimeElements != null) {
                String dateTimeString = dateTimeElements.text().trim();
                String regexString = "on ";
                int indexOfRegex = dateTimeString.indexOf(regexString);
                int indexOfSpace = dateTimeString.indexOf(" ", indexOfRegex + regexString.length());
                if (indexOfRegex > 0 && indexOfSpace > 0) {
                    dateTimeString = dateTimeString.substring(indexOfRegex + regexString.length()).trim().toLowerCase();
                    String unformatted = dateTimeString.split(" ")[1];
                    if (unformatted.length() == 7) {
                        String formatted = "0" + unformatted;
                        dateTimeString = dateTimeString.replace(unformatted, formatted);
                    }
                }
                documentCrawler.setDateTime(dateTimeString.replaceAll("/", "-"));
            }
            //--------------------------------------------------------------------------------------------------------//
            Elements authorElements = bodyElement.select("div#author-desc");
            if (authorElements.first() != null) {
                String authorString = authorElements.first().getElementsByTag("h4").text().replace("About ", "").trim();
                documentCrawler.setAuthor(authorString);
            }
            //--------------------------------------------------------------------------------------------------------//
            Elements tagElements = bodyElement.select("div.post-tags");
            if (tagElements.first() != null) {
                StringBuffer tagStrings = new StringBuffer();
                for (Element tag : tagElements.first().getElementsByTag("a")) {
                    tagStrings.append(tag.text());
                    tagStrings.append(",");
                }
                if (tagStrings.length() > 0) {
                    tagStrings.deleteCharAt(tagStrings.length() - 1);
                }
                documentCrawler.setTags(tagStrings.toString());
            }
            //--------------------------------------------------------------------------------------------------------//
            documentCrawler.printDocument();
            documentCrawler.writeDocument(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documentCrawler;
    }

}
