import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReptilePicUtil {


    public static final WebClient initWebClient() {
        WebClient webClient = new WebClient(BrowserVersion.CHROME);

        webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常
        webClient.getOptions().setActiveXNative(false);
        webClient.getOptions().setCssEnabled(false);//是否启用CSS
        webClient.getOptions().setJavaScriptEnabled(false); //很重要，启用JS
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX

        webClient.getOptions().setTimeout(30000);//设置“浏览器”的请求超时时间
        webClient.setJavaScriptTimeout(30000);//设置JS执行的超时时间
        return webClient;
    }

    /**
     * 获取页面文档字串(等待异步JS执行)
     *
     * @param url 页面URL
     * @return
     * @throws Exception
     */
    public static String getHtmlPageResponse(String url) throws Exception {
        String result = "";
        HtmlPage page;
        try {
            page = ReptilePicUtil.initWebClient().getPage(url);
            // HtmlTextInput start =(HtmlTextInput) page.getByXPath("/html/body/div[2]/div[6]/div/div/div/div[1]/form/div/ul/li[3]/span[2]/span[1]/span/input");
            // start.setValueAttribute("20");
            //System.out.println(page.getByXPath("/html/body/div[1]/div[1]/div[2]/div/div[3]/div[1]/input").get(0));

         /*       for (int i = 0; i < tag; i++) {
                    HtmlAnchor anchor = (HtmlAnchor) page.getByXPath("/html/body/div[1]/div[3]/div[2]/div/a[2]").get(0);
                    page = anchor.click();
            }
            System.out.println("点击了"+tag+"次");*/

        } catch (Exception e) {
            ReptilePicUtil.initWebClient().close();
            throw e;
        }
        ReptilePicUtil.initWebClient().waitForBackgroundJavaScript(30000);//该方法阻塞线程

        result = page.asXml();
        ReptilePicUtil.initWebClient().close();

        return result;
    }


    //解析xml获取ImageUrl地址
    public static List<String> getImageUrl(String html, String tagName) {
        List<String> result = new ArrayList<>();
        Document document = Jsoup.parse(html);//获取html文档
        List<Element> infoListEle = document.getElementsByTag(tagName);//获取元素节点等
        infoListEle.forEach(element -> {
            result.add(element.text());
        });
        //tring str =infoListEle.children().text();
        return result;
    }

    //解析xml获取ImageUrl地址
    public static String getByXpath(String html, String className, int tag) {
        List<String> result = new ArrayList<>();
        Document document = Jsoup.parse(html);//获取html文档
        //System.out.println(document);
        Elements e = document.getElementsByClass(className);//获取元素节点等
        String out = "";
        //tring str =infoListEle.children().text();
        if (tag == 0) {
            for (int i = 1; i < e.size(); i++) {
                if (i == e.size() - 1)
                    out = out + e.get(i).text();
                else
                    out = out + e.get(i).text() + " ";
            }
        } else
            out = e.text();
        return out;
    }

    public static Map<String, Object> outStrDecode(int tag, String str) {
        System.out.println(tag);
        String years[] = {
                "2011", "2012", "2013", "2014", "2015", "2016", "2017", "2018", "2019", "2020", "2021","2022","total"
        };
        String strArr[] = str.split(" ");
        Map<String, Object> map = new HashMap<>();
        int countArr[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,tag};
        for (int i = 6; i < strArr.length; i++) {
            if (!strArr[i].contains("年")) {
                countArr[11]++;
            }
            if (strArr[i].contains("2021")) {
                countArr[10]++;
            }
            if (strArr[i].contains("2020")) {
                countArr[9]++;
            }
            if (strArr[i].contains("2019")) {
                countArr[8]++;
            }
            if (strArr[i].contains("2018")) {
                countArr[7]++;
            }
            if (strArr[i].contains("2017")) {
                countArr[6]++;
            }
            if (strArr[i].contains("2016")) {
                countArr[5]++;
            }
            if (strArr[i].contains("2015")) {
                countArr[4]++;
            }
            if (strArr[i].contains("2014")) {
                countArr[3]++;
            }
            if (strArr[i].contains("2013")) {
                countArr[2]++;
            }
            if (strArr[i].contains("2012")) {
                countArr[1]++;
            }
            if (strArr[i].contains("2011")) {
                countArr[0]++;
            }
        }
        for (int i = 0; i < countArr.length; i++) {
            map.put(years[i], countArr[i]);
        }
        return map;
    }

    public static void main(String[] args) throws Exception {
        String banks[] = {"齐商银行", "温州银行", "攀枝花市商业银行","唐山银行","深圳农村商业银行", "北京农村商业银行", "桂林银行", "台州银行", "吉林银行", "福建海峡银行", "绍兴银行", "四川银行", "齐鲁银行", "兰州银行"};
     String keyWords[] = {
                "第三方支付", "移动支付", "在线支付", "网上支付", "手机支付", "NFC支付", "数字货币", "网络融资", "网络贷款", "众筹", "网贷", "互联网金融", "消费金融", "在线理财",
                "互联网理财", "互联网保险", "智能投顾", "征信", "开放银行", "在线银行", "网上银行", "网银", "电子银行", "手机银行", "移动互联", "机器学习", "深度学习", "区块链", "智能",
                "生物识别", "大数据", "云计算", "数据挖掘", "语音识别", "5G", "人脸识别"
        };
       int totalLength=keyWords.length* banks.length;//数据集总大小
        int count = 0;
        Map<String, Map<String, Object>> resultMap = new HashMap<>();
        for (int i = 0; i < banks.length; i++) {

            for (int j = 0; j < keyWords.length; j++) {
                String outStr = "";
                String url = "https://www.baidu.com/s?rtt=4&bsst=1&cl=2&tn=news&ie=utf-8&word=title: (\"" + banks[i] + "%2B" + keyWords[j] + "\")&pn=0";
                System.out.println(url);
                String xml = ReptilePicUtil.getHtmlPageResponse(url);
                String text = ReptilePicUtil.getByXpath(xml, "c-color-gray2", 1);
                count++;
                System.out.println("进度：" + count + "/"+totalLength);

                outStr = outStr + text + " ";
                System.out.println(outStr);
                String newsNum = text.split(" ")[1];
                String regEx = "[^0-9]";
                Pattern p = Pattern.compile(regEx);
                Matcher m = p.matcher(newsNum);
                double tag = Double.parseDouble(m.replaceAll("").trim());
                int clickNum = (int) Math.ceil(tag / 10.0);
                for (int k = 1; k < clickNum; k++) {
                    String xml1 = ReptilePicUtil.getHtmlPageResponse("https://www.baidu.com/s?rtt=4&bsst=1&cl=2&tn=news&ie=utf-8&word=title: (\"" + banks[i] + "%2B" + keyWords[j] + "\")&pn=" + k * 10);

                    String text1 = ReptilePicUtil.getByXpath(xml1, "c-color-gray2", 0);
                    if (k == clickNum - 1)
                        outStr = outStr + text1;
                    else
                        outStr = outStr + text1 + " ";
                }
                outStr = banks[i] + "+" + keyWords[j] + "---" + outStr;
                //System.out.println(outStr);

                Map map = ReptilePicUtil.outStrDecode((int) tag, outStr);
                resultMap.put(banks[i]+"+"+keyWords[j], map);
            }

        }
        JSONObject obj = JSONObject.parseObject(JSON.toJSONString(resultMap));
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter fw = new FileWriter("C://Users/chen/Desktop/outPutResult.json");
            gson.toJson(obj, fw);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}


