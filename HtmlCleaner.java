package searchengine.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

public class HtmlCleaner {

    public static String clear(String content, String selector) {
        StringBuilder html = new StringBuilder();       // StringBuilder to store cleaned HTML code
        var doc = Jsoup.parse(content);                 // Parse the input content as an HTML document
        var elements = doc.select(selector);            // Select elements matching the provided selector
        for (Element el : elements) {
            html.append(el.html());                     // Append the HTML code of each selected element
        }
        return Jsoup.parse(html.toString()).text();     // Parse the accumulated HTML and retrieve the plain text
    }
}
