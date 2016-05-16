package edu.epu.sentiment.analysis.crawler;

import edu.epu.sentiment.analysis.utils.SAFile;
import edu.epu.sentiment.analysis.utils.SALog;
import edu.epu.sentiment.analysis.utils.SAString;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by duong on 3/29/16.
 */
public class SADocumentCrawler {

    public static final String DOCUMENT_EXTENSION = ".txt";
    private String url;
    private String title;
    private String author;
    private String dateTime;
    private String body;
    private String tags;
    private SADelegateCrawler callback;
    private String review;
    private String rating;
    private String product;
    private String productDescription;
    private String textComment;
    private String ratingProduct;
    private String startPage;
    private String endPage;

    public String getStartPage() {
        return startPage;
    }

    public void setStartPage(String startPage) {
        this.startPage = startPage;
    }

    public String getEndPage() {
        return endPage;
    }

    public void setEndPage(String endPage) {
        this.endPage = endPage;
    }


    public String getRatingProduct() {
        return ratingProduct;
    }

    public void setRatingProduct(String ratingProduct) {
        this.ratingProduct = ratingProduct;
    }



    public String getTextComment() {
        return textComment;
    }
    public void setTextComment(String textComment) {
        this.textComment = textComment;
    }
    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }


    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }


    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public SADocumentCrawler(String url) {
        this.url = url;
        title = "";
        author = "";
        dateTime = "";
        body = "";
        tags = "";
    }

    public static Document getDocumentFromUrl(String url) throws IOException {
        return Jsoup.connect(url)
                .ignoreContentType(true)
                .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
                .referrer("http://www.google.com")
                .timeout(12000)
                .followRedirects(true)
                .get();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public SADelegateCrawler getCallback() {
        return callback;
    }

    public void setCallback(SADelegateCrawler callback) {
        this.callback = callback;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void writeDocument(String folder) {
        String file = creatStorageFolder(folder) + File.separator + SAString.normalizeTitle(title) + DOCUMENT_EXTENSION;
        ArrayList<String> strings = new ArrayList<String>();
        strings.add(title);
        strings.add(SAFile.line);
        strings.add(product + "");
        strings.add(SAFile.line);
        strings.add(productDescription + "");
        strings.add(SAFile.line);
        strings.add(review + "");
        strings.add(SAFile.line);
        strings.add(rating + "");
        strings.add(SAFile.line);
        strings.add(ratingProduct + "");
        strings.add(SAFile.line);
        strings.add(startPage + "");
        strings.add(SAFile.line);
        strings.add(endPage + "");
        strings.add(SAFile.line);

        File docFile = new File(file);
        if (docFile.exists() == true) {
            docFile.delete();
        }
        SAFile.writeStringsToFile(strings, file);
    }

    public String creatStorageFolder(String rootFolder) {
        String[] dateStrings = dateTime.split(" ")[0].split("-");
        StringBuffer newFolder = new StringBuffer();
        for (int i = dateStrings.length - 1; i >= 0; i--) {
            newFolder.append(dateStrings[i]);
            newFolder.append("-");
        }
        newFolder.deleteCharAt(newFolder.length() - 1);
        rootFolder += File.separator + newFolder;
        File subFile = new File(rootFolder);
        if (subFile.exists() == false) {
            subFile.mkdir();
        }
        SALog.log("FOLDER", "Created folder: " + rootFolder);
        return rootFolder;
    }

    public void printDocument() {
        // SALog.log("URL", url);
        SALog.log("TITLE", title);
        SALog.log("REVIEW", review + "");
        SALog.log("PRODUCT", product + "");
        SALog.log("PRODUCTDESCRIPTION", productDescription + "");
        SALog.log("RATING", rating + "");
        SALog.log("ratingPRODUCT", ratingProduct + "");
        SALog.log("startPage",startPage + "");
        SALog.log("endPage", endPage + "");
    }

}
