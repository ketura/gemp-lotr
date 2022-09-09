package com.gempukku.lotro.league;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.LotroFormat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SealedLeagueDefinition {


    //TODO:
    // + flesh this out and convert NewSealedLeagueData to utilize it
    // + Add sealed format loading to LotroFormatLibrary
    // + Add hot reloading to LotroFormatLibrary / extend the hot reload button
    // - Rename LotroFormatLibrary -> FormatLibrary
    // + Alter admin panel usage to look up instead of recreating each time
    // x move the static creation to FormatLibrary
    // - add 5 second delay scrolldown on chat creation?
    // - add resize handle to chat
    // - Alter the league admin panel to dynamically generate all three format dropdowns
    // -

    private final String _name;
    private final LotroFormat _format;
    private final List<List<CardCollection.Item>> _seriesProduct = new ArrayList<>();

    public SealedLeagueDefinition(String name, LotroFormat format, List<List<String>> product) {
        _name = name;
        _format = format;

        for(var serie : product) {
            List<CardCollection.Item> items = new ArrayList<>();
            for(String def : serie) {
                var item = CardCollection.Item.createItem(def);
                items.add(item);
            }

            _seriesProduct.add(items);
        }
    }

    public int GetSerieCount() { return _seriesProduct.size(); }

    public String GetName() { return _name; }
    public LotroFormat GetFormat() { return _format; }
    public List<List<CardCollection.Item>> GetAllSeriesProducts() { return Collections.unmodifiableList(_seriesProduct); }
    public List<CardCollection.Item> GetProductForSerie(int serie) { return Collections.unmodifiableList(_seriesProduct.get(serie)); }
}
