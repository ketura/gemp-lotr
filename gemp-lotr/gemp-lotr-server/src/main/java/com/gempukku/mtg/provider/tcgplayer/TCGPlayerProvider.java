package com.gempukku.mtg.provider.tcgplayer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TCGPlayerProvider {
    private List<String> downloadSetList() throws IOException {
        List<String> results = new LinkedList<String>();
        Document doc = Jsoup.connect("http://shop.tcgplayer.com/magic").get();
        for (Element element : doc.select("#SetName option")) {
            String setName = element.attr("value");
            if (!setName.equals("All"))
                results.add(setName);
        }
        return results;
    }
}
