package edu.epu.sentiment.analysis.crawler;

import edu.epu.sentiment.analysis.utils.SADate;
import edu.epu.sentiment.analysis.utils.SAFile;
import edu.epu.sentiment.analysis.utils.SALog;
import edu.epu.sentiment.analysis.utils.SAString;
import org.jcp.xml.dsig.internal.dom.Utils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by duong on 3/29/16.
 */
public class SAAmazonCrawler extends SABaseCrawler {

    public SAAmazonCrawler(long sleepTime, String[] folders) {
        super(sleepTime, folders);
        //them trang can lay
        // đường link phải được giải mã tránh ký tự % của link mã hóa
        groups.add(new SAGroupCrawler("http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias=aps&field-keywords=coffee&rh=i:aps,k:coffee", "http://www.amazon.com/s/ref=sr_pg_2?fst=as:on&rh=k:coffee,n:16310101,n:16310231,n:16318031&page=%s&keywords=coffee&ie=UTF8&qid=1460654831&spIA=B0174E22DM,B00LV2O7ZK,B01B23SGFE",1,1, 1));
       // groups.add(new SAGroupCrawler("http://www.amazon.com/s/ref=nb_sb_ss_c_0_12?url=search-alias=aps&field-keywords=coffeetrungnguyen&sprefix=coffee trung nguyen,aps,401&rh=i:aps,k:coffee trung nguyen","http://www.amazon.com/s/ref=sr_pg_2?rh=i:aps,k:coffee trung nguyen&page=%s&keywords=coffee trung nguyen&ie=UTF8&qid=1461776330",1,1, 1));
        //groups.add("http://www.amazon.com/Trung-Nguyen-Vietnamese-coffee-can/dp/B000F17AKC/ref=sr_1_1_a_it?ie=UTF8&qid=1461776330&sr=8-1&keywords=coffee trung nguyen");
    }

    /**
     * lay link con
     * @param parentUrl:duong dan
     * @return
     */
    @Override
    public ArrayList<String> getChildUrls(String parentUrl) {
        ArrayList<String> urls = new ArrayList<String>();
        try {
            Document document = SADocumentCrawler.getDocumentFromUrl(parentUrl);
            Element body = document.body();// lấy phần body
            Elements elements = body.select("a.a-link-normal.s-access-detail-page.a-text-normal");
            //System.out.println(elements);
            for (Element element : elements) {
                String href = element.attr("href").trim();
                if (href.contains("http://www.amazon.com")) {
                    urls.add(href);
                    SALog.log("GET", href);
                }
            }
            SALog.log("Tổng số", urls.size() + "");
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
            Elements titleElements=bodyElement.select("span#productTitle");
            String title=titleElements.text().trim();
            documentCrawler.setTitle(title);//dat tieu de
            // Tạo thư muacj theo tiêu đề
            String folder = directory + File.separator + SAString.normalizeTitle(title);
            File file = new File(folder);
            if (file.exists() == false) {
                file.mkdir();
            }

            //tao thu muc comment

            String folderComment= documentCrawler.creatStorageFolder(folder) + File.separator +"Comment_"+ SAString.normalizeTitle(title);
            File folder_comment = new File(folderComment);
            if (folder_comment.exists() == false) {
                folder_comment.mkdir();
            }
            //--------------------------------------------------------------------------------------------------------//
            //4.5 out of 5 stars 16,551 -> 4.5 16,551
            Elements aElements=bodyElement.select("div#avgRating");
            String reviewString = aElements.text().trim();
            String[] reviewStrings = reviewString.split(" ");//tach thanh cac mang
            reviewStrings[0]="Rating:"+reviewStrings[0];
            documentCrawler.setRating(reviewStrings[0]);
            //--------------------------------------------------------------------------------------------------------//
            Elements ratings = bodyElement.select("tr.a-histogram-row");
            String b="";
            for (Element e: ratings) {
                String a=e.select("td.a-nowrap").text();b=a+";"+b;
                // documentCrawler.setRatingProduct(a);

            }
            b="RatingProduct:"+b;
            documentCrawler.setRatingProduct(b);;
            //--------------------------------------------------------------------------------------------------------//
            //About the Product
            Elements productElements=bodyElement.select("div#fbSection");
            String productString = productElements.text().trim();
            productString="Product:"+productString;
            documentCrawler.setProduct(productString);
            //--------------------------------------------------------------------------------------------------------//
            //About the productDescription
            Elements productDescriptionElements=bodyElement.select("div.bucket>.content:nth-child(3)");
            String productDescriptionString = productDescriptionElements.text().trim();
            productDescriptionString= "About the productDescription :"+productDescriptionString;
            documentCrawler.setProductDescription(productDescriptionString);
            //--------------------------------------------------------------------------------------------------------//
            Elements reviewElements=bodyElement.select("span#acrCustomerReviewText");
            String reviewCustomer=reviewElements.text().trim();
            String[] reviewCustomers=reviewCustomer.split(" ");
            String reviews_P=reviewCustomers[0];
            reviewCustomers[0]="Reviews:"+reviewCustomers[0];
            documentCrawler.setReview(reviewCustomers[0]);
            //--------------------------------------------------------------------------------------------------------//
            //count page

            String startPage="1";
            documentCrawler.setStartPage(startPage);
            int review_Customer=Integer.parseInt(reviews_P);
            if( review_Customer%10!=0) {
                int end_Page = review_Customer/10+1;
                String endPage=end_Page+"";
                documentCrawler.setEndPage(endPage);
            }
            else
            {
                int end_Page = review_Customer/10;
                String endPage=end_Page+"";
                documentCrawler.setEndPage(endPage);
            }
            //--------------------------------------------------------------------------------------------------------//
            Elements reviewComments = bodyElement.select("div.a-section.celwidget");
            reviewComments.remove(0);
            for (Element e: reviewComments) {
                // tạo thư mục theo từng sản phẩm
                // lấy thông tin của từng người comment
                String content = "";
                String name = e.select("a.noTextDecoration").text();
                content +="Name:"+ name;
                content += SAFile.line;
                // ghi thông tin comment vào tệp mang tên người comment
                //String subFolder = folder + File.separator + name;
                //File subFile = new File(subFolder);
                //if (subFile.exists() == false) {
                //subFile.mkdir();
                // }
                // chuan hoa xau de lay gia tri can thiet:5.0 out of 5 stars -> 5.0
                //--------------------------------------------------------------------------------------------------------//
                String rate = e.select("span.a-icon-alt").text();
                //tach thanh cac mang chua cac tu cach nhau boi khoang cach va lay phan tu dau tien cua mang
                rate = rate.substring(0, rate.indexOf(" "));
                //noi chuoi
                content += "Rating:"+rate;
                content += SAFile.line;//in ky tu xuong dong
                //--------------------------------------------------------------------------------------------------------//
                // String textComment = e.select("div.a-row.a-spacing-small>.a-section").text();
                String textComment = e.select("div.a-row.a-spacing-small>.a-section").html();
                if(!textComment.contains("Read more"))
                {
                    textComment=e.select("div.a-row.a-spacing-small>.a-section").text();
                }
                else {
                    textComment = textComment.replace("<span class=\"MHRHead\">", "");
                    textComment = textComment.replace("<br>", "");
                    textComment = textComment.replace("</span>", "");
                    textComment = textComment.replace("<span class=\"a-declarative\" data-action=\"columnbalancing-showfullreview\" data-columnbalancing-showfullreview=\"{&quot;rest&quot;:&quot;", "");
                    textComment = textComment.replace("<br />", "");
                    // textComment=textComment.replace("<a class=\"a-link-normal\" href=\"http://www.amazon.com/Trung-Nguyen-Vietnamese-coffee-can/product-reviews/B000F17AKC/ref=cm_cr_dp_text/176-0933294-8113706?ie=UTF8&amp;showViewpoints=0&amp;sortBy=helpful#R24SMTRF5MBWK9\"><span class=\"a-letter-space\"></span>Read more ›</a></span>"," ");
                    //noi chuoi
                    String str[] = textComment.split("&quot;");
                    textComment = str[0];
                }
                content +="Comment:"+ textComment;
                content += SAFile.line;//in ky tu xuong dong
                //--------------------------------------------------------------------------------------------------------//
                String textPeopleHelpful = e.select("span.votingStripe>.a-size-base.a-color-secondary.cr-vote-buttons").text();
                //noi chuoi
                content +="textPeopleHelpful:"+ textPeopleHelpful;
                content += SAFile.line;//in ky tu xuong dong
                //--------------------------------------------------------------------------------------------------------//
                String countComment = e.select("a.a-link-normal.comment-link.commentStripe").text();
                //noi chuoi
                content +="countComment:"+ countComment;
                content += SAFile.line;//in ky tu xuong dong
                //--------------------------------------------------------------------------------------------------------//
                String date = e.select("span.a-color-secondary").get(2).text();
                date = date.replace("on ", "");//thay the ki chuoi"on " bằng ""
                content +="Date:"+ date;
                content += SAFile.line;
                //--------------------------------------------------------------------------------------------------------//
                //tao file co duoi.txt trong thu muc vua tao
                String fileName = folder_comment + File.separator + SAString.normalizeTitle(name) + "_" + SAString.normalizeTitle(date) +".txt";
                File newFile = new File(fileName);
                //neu file do da ton tai thi xoa  thay bang thu muc moi
                if (newFile.exists() == true) {
                    newFile.delete();
                }
                //chuan hoa file loai cac ky tu dac biet
                SAFile.writeStringToFile(content, fileName);
            }
            //--------------------------------------------------------------------------------------------------------//
            documentCrawler.printDocument();
            documentCrawler.writeDocument(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return documentCrawler;
    }

}
