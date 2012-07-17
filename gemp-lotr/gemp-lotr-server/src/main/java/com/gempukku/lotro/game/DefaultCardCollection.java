package com.gempukku.lotro.game;

import com.gempukku.lotro.packs.PacksStorage;

import java.util.*;

public class DefaultCardCollection implements MutableCardCollection {
    private Map<String, Item> _counts = new LinkedHashMap<String, Item>();
    private int _currency;

    public DefaultCardCollection() {

    }

    public DefaultCardCollection(CardCollection cardCollection) {
        _counts.putAll(cardCollection.getAll());
        _currency = cardCollection.getCurrency();
    }

    @Override
    public synchronized void addCurrency(int currency) {
        _currency += currency;
    }

    @Override
    public synchronized boolean removeCurrency(int currency) {
        if (_currency < currency)
            return false;
        _currency -= currency;
        return true;
    }

    @Override
    public synchronized int getCurrency() {
        return _currency;
    }

    @Override
    public synchronized void addItem(String itemId, int toAdd) {
        Item oldCount = _counts.get(itemId);
        if (oldCount == null)
            _counts.put(itemId, Item.createItem(itemId, toAdd));
        else
            _counts.put(itemId, Item.createItem(itemId, toAdd + oldCount.getCount()));
    }

    @Override
    public synchronized boolean removeItem(String itemId, int toRemove) {
        Item oldCount = _counts.get(itemId);
        if (oldCount == null || oldCount.getCount() < toRemove)
            return false;
        if (oldCount.getCount() == toRemove)
            _counts.remove(itemId);
        else
            _counts.put(itemId, Item.createItem(itemId, oldCount.getCount() - toRemove));
        return true;
    }

    @Override
    public synchronized CardCollection openPack(String packId, String selection, PacksStorage packsStorage) {
        Item count = _counts.get(packId);
        if (count == null)
            return null;
        if (count.getCount() > 0) {
            List<Item> packContents = null;
            if (selection != null && packId.startsWith("(S)")) {
                if (hasSelection(packId, selection, packsStorage)) {
                    packContents = new LinkedList<Item>();
                    packContents.add(Item.createItem(selection, 1));
                }
            } else {
                packContents = packsStorage.openPack(packId);
            }

            if (packContents == null)
                return null;

            DefaultCardCollection packCollection = new DefaultCardCollection();

            for (Item itemFromPack : packContents) {
                addItem(itemFromPack.getBlueprintId(), itemFromPack.getCount());
                packCollection.addItem(itemFromPack.getBlueprintId(), itemFromPack.getCount());
            }

            removeItem(packId, 1);

            return packCollection;
        }
        return null;
    }

    @Override
    public synchronized Map<String, Item> getAll() {
        return Collections.unmodifiableMap(_counts);
    }

    @Override
    public synchronized int getItemCount(String blueprintId) {
        Item count = _counts.get(blueprintId);
        if (count == null)
            return 0;
        return count.getCount();
    }

    private boolean hasSelection(String packId, String selection, PacksStorage packsStorage) {
        for (Item item : packsStorage.openPack(packId)) {
            if (item.getBlueprintId().equals(selection))
                return true;
        }
        return false;
    }
}
