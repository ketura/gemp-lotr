package com.gempukku.lotro.game;

import com.gempukku.lotro.packs.PacksStorage;

import java.util.*;

public class DefaultCardCollection implements MutableCardCollection {
    private final Map<String, Item> _counts = new LinkedHashMap<>();
    private int _currency;
    private Map<String, Object> _extraInformation = new HashMap<>();

    public DefaultCardCollection() {

    }

    public DefaultCardCollection(CardCollection cardCollection) {
        for (Item item : cardCollection.getAll()) {
            _counts.put(item.getBlueprintId(), item);
        }

        _currency = cardCollection.getCurrency();
        _extraInformation.putAll(cardCollection.getExtraInformation());
    }

    public synchronized void setExtraInformation(Map<String, Object> extraInformation) {
        _extraInformation = extraInformation;
    }

    @Override
    public synchronized Map<String, Object> getExtraInformation() {
        return Collections.unmodifiableMap(_extraInformation);
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
        if (toAdd > 0) {
            Item oldCount = _counts.get(itemId);
            if (oldCount == null) {
                _counts.put(itemId, Item.createItem(itemId, toAdd));
            } else
                _counts.put(itemId, Item.createItem(itemId, toAdd + oldCount.getCount()));
        }
    }

    @Override
    public synchronized boolean removeItem(String itemId, int toRemove) {
        if (toRemove > 0) {
            Item oldCount = _counts.get(itemId);
            if (oldCount == null || oldCount.getCount() < toRemove)
                return false;
            if (oldCount.getCount() == toRemove) {
                _counts.remove(itemId);
            } else
                _counts.put(itemId, Item.createItem(itemId, oldCount.getCount() - toRemove));
        }
        return true;
    }

    @Override
    public synchronized CardCollection openPack(String packId, String selection, PacksStorage packsStorage) {
        Item count = _counts.get(packId);
        if (count == null)
            return null;
        if (count.getCount() > 0) {
            List<Item> packContents = null;
            if (packId.startsWith("(S)")) {
                if (selection != null && hasSelection(packId, selection, packsStorage)) {
                    packContents = new LinkedList<>();
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
    public synchronized Iterable<Item> getAll() {
        return _counts.values();
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
