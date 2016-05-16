package edu.epu.sentiment.analysis.crawler;

import edu.epu.sentiment.analysis.utils.SADate;
import edu.epu.sentiment.analysis.utils.SALog;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by duong on 3/29/16.
 */
public class SAVietBaoVnCrawler extends SABaseCrawler {

    public SAVietBaoVnCrawler(long sleepTime, String[] folders) {
        super(sleepTime, folders);
        groups.add(new SAGroupCrawler("http://vietbao.vn/top/Thi-truong-vang/", "http://vietbao.vn/top/Thi-truong-vang/%s/", 2, 199, 1));
    }

    @Override
    public ArrayList<String> getChildUrls(String parentUrl) {
        ArrayList<String> urls = new ArrayList<String>();
        try {
            Document document = SADocumentCrawler.getDocumentFromUrl(parentUrl);
            Elements elements = document.select("div#vb-content-detailbox");
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
            Elements titleElements = bodyElement.select("h1");
            String titleString = titleElements.text().trim();
            documentCrawler.setTitle(titleString);
            //--------------------------------------------------------------------------------------------------------//
            Elements bodyElements = bodyElement.select("span.VB_noi_dung");
            if (bodyElements.first() != null) {
                Elements trashElements = bodyElements.first().getElementsByTag("div");
                if (trashElements != null) {
                    trashElements.remove();
                }
            }
            documentCrawler.setBody(bodyElements.text().trim());
            //--------------------------------------------------------------------------------------------------------//
            Elements dateTimeElements = bodyElement.select("div.postby.clearfix");
            if (dateTimeElements != null) {
                String dateTimeString = dateTimeElements.text().trim().toLowerCase();
                String[] dateTimeStrings = dateTimeString.split(",");
                if (dateTimeStrings.length == 3) {
                    String dateString = dateTimeStrings[1].trim();
                    for (String key : SADate.months.keySet()) {
                        if (dateString.contains(key)) {
                            dateString = dateString.replace(key, SADate.months.get(key));
                            dateString = dateString.replace(" ", "-");
                            break;
                        }
                    }
                    String timeString = dateTimeStrings[2].trim();
                    timeString = timeString.substring(0, timeString.indexOf(" "));
                    if (timeString.length() == 5) {
                        timeString += ":00";
                    }
                    documentCrawler.setDateTime(dateString + " " + timeString);
                } else {
                    Date date = new Date();
                    DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    documentCrawler.setDateTime(dateFormat.format(date));
                }
            }
            //--------------------------------------------------------------------------------------------------------//
            Elements authorElements = bodyElement.select("div.vivamain181");
            if (authorElements != null) {
                String authorString = authorElements.text().trim();
                documentCrawler.setAuthor(authorString);
            }
            //--------------------------------------------------------------------------------------------------------//
            Elements tagElements = bodyElement.select("div.tag");
            if (tagElements != null) {
                Element tagElement = tagElements.first();
                if (tagElement != null) {
                    StringBuffer tagStrings = new StringBuffer();
                    for (Element aTagElement : tagElement.getElementsByTag("a")) {
                        tagStrings.append(aTagElement.text().trim());
                        tagStrings.append(",");
                    }
                    if (tagStrings.length() > 0) {
                        tagStrings.deleteCharAt(tagStrings.length() - 1);
                    }
                    documentCrawler.setTags(tagStrings.toString());
                }
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
