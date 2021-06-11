package com.ysw.amcrawler;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class AmcrawlerApplicationTests {

    @Test
    void contextLoads() throws InterruptedException {
        System.getProperties().setProperty("webdriver.chrome.driver","D:\\Program Files (x86)\\devinstall\\chromedriver_win321\\chromedriver.exe");
        //开启webDriver进程
        WebDriver webDriver = new ChromeDriver();
        webDriver.get("https://www.amazon.com/Machines-Masticating-Extractor-Function-Vegetables/product-reviews/B07DCKYJ5D/ref=cm_cr_dp_d_show_all_btm?ie=UTF8&reviewerType=all_reviews");
        WebElement criticalViewDiv=webDriver.findElement(By.className("critical-review"));
        List<WebElement> criticalList=criticalViewDiv.findElements(By.tagName("a"));
        criticalList.get(0).click();
        Thread.sleep(2000);
        getSource(webDriver);
        //关闭webDriver进程
        webDriver.close();
        webDriver.quit();
        System.out.println("aaa-bbb");
    }
    private static void getSource(WebDriver webDriver) throws InterruptedException {

        //String source = webDriver.getPageSource();

        //总体评论DIV
        WebElement element = webDriver.findElement(By.id("cm_cr-review_list"));
        List<WebElement> elements = element.findElements(By.tagName("div"));
        for (WebElement webElement : elements) {
            String className = webElement.getAttribute("class");
            //过滤找到对应评论列表
            if (!"a-section review aok-relative".equalsIgnoreCase(className)){
                continue;
            }
            //对应评论信息
           /* WebElement userNameEle = webElement.findElement(By.className("a-profile-name"));
            System.out.println("用户:"+userNameEle.getText());
            WebElement aRowEle = webElement.findElement(By.className("a-row"));
            WebElement starNumEle = aRowEle.findElement(By.className("a-link-normal"));
            System.out.println("星数:"+starNumEle.getAttribute("title"));
            List<WebElement> list = aRowEle.findElements(By.tagName("a"));
            WebElement starLevelEle = list.stream().filter(ele -> ele.getAttribute("class").equalsIgnoreCase("a-size-base a-link-normal review-title a-color-base review-title-content a-text-bold")).collect(Collectors.toList()).get(0);
            System.out.println("星级:"+starLevelEle.getText());
            WebElement creatMessEle = webElement.findElements(By.tagName("span")).stream().filter(ele->ele.getAttribute("class").equalsIgnoreCase("a-size-base a-color-secondary review-date")).collect(Collectors.toList()).get(0);
            System.out.println("评论时间:" + creatMessEle.getText());*/
            WebElement contentEle = webElement.findElements(By.tagName("div")).stream().filter(ele->ele.getAttribute("class").equalsIgnoreCase("a-row a-spacing-small review-data")).collect(Collectors.toList()).get(0);

            System.out.println("评论:" + contentEle.getText());
            System.out.println("--------------------------------------");



        }
        WebElement pageBar = webDriver.findElement(By.id("cm_cr-pagination_bar"));
        List<WebElement> pageList=pageBar.findElements((By.tagName("li")));
        WebElement webElement=pageList.stream().filter(x->x.getAttribute("class").equals("a-last")).findFirst().orElse(null);
        WebElement pageNext=webElement.findElement(By.tagName("a"));
        if(pageNext!=null){
            pageNext.click();
            Thread.sleep(6000);
            getSource(webDriver);
        }
    }

    @Test
    public void translatorTest() throws Exception {
        System.out.println(translate("en","zh-CN","hello world"));
    }



        public static  String translate(String langFrom, String langTo,
                                String word) throws Exception {

            String url = "https://translate.googleapis.com/translate_a/single?" +
                    "client=gtx&" +
                    "sl=" + langFrom +
                    "&tl=" + langTo +
                    "&dt=t&q=" + URLEncoder.encode(word, "UTF-8");

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            return parseResult(response.toString());
        }

        private static  String parseResult(String inputJson) throws Exception {
            /*
             * inputJson for word 'hello' translated to language Hindi from English-
             * [[["नमस्ते","hello",,,1]],,"en"]
             * We have to get 'नमस्ते ' from this json.
             */

            JSONArray jsonArray =  JSON.parseArray(inputJson);
            JSONArray jsonArray2 = (JSONArray) jsonArray.get(0);
//        JSONArray jsonArray3 = (JSONArray) jsonArray2.get(0);
            String result ="";

            for(int i =0;i < jsonArray2.size();i ++){
                result += ((JSONArray) jsonArray2.get(i)).get(0).toString();
            }

            return result;
        }




}
