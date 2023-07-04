package com.ll.gildong.Util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

public class HtmlUtils {
    public static String removePTags(String html) {
        Document document = Jsoup.parse(html);
        Elements paragraphs = document.select("p");
        for (Element paragraph : paragraphs) {
            paragraph.unwrap(); // <p> 태그를 제거하고 내용을 유지
        }
        String cleanedHtml = Jsoup.clean(document.html(), Whitelist.none());
        return cleanedHtml;
    }
}