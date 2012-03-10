package com.gempukku.lotro.game;

import com.gempukku.lotro.packs.PacksStorage;

import java.util.*;

public class DefaultCardCollection implements MutableCardCollection {
    private Map<String, Integer> _counts = new LinkedHashMap<String, Integer>();

    public DefaultCardCollection() {

    }

    public DefaultCardCollection(CardCollection cardCollection) {
        _counts.putAll(cardCollection.getAll());
    }

    @Override
    public void addItem(String itemId, int count) {
        Integer oldCount = _counts.get(itemId);
        if (oldCount == null)
            oldCount = 0;
        _counts.put(itemId, oldCount + count);
    }

    @Override
    public boolean removeItem(String itemId, int count) {
        Integer oldCount = _counts.get(itemId);
        if (oldCount == null || oldCount < count)
            return false;
        final int newCount = oldCount - count;
        if (newCount == 0)
            _counts.remove(itemId);
        else
            _counts.put(itemId, newCount);
        return true;
    }

    private boolean hasSelection(String packId, String selection, PacksStorage packsStorage) {
        for (Item item : packsStorage.openPack(packId)) {
            if (item.getBlueprintId().equals(selection))
                return true;
        }
        return false;
    }

    @Override
    public synchronized CardCollection openPack(String packId, String selection, PacksStorage packsStorage) {
        Integer count = _counts.get(packId);
        if (count == null)
            return null;
        if (count > 0) {
            List<Item> packContents = null;
            if (selection != null && packId.startsWith("(S)")) {
                if (hasSelection(packId, selection, packsStorage)) {
                    packContents = new LinkedList<Item>();
                    Item.Type type = selection.contains("_") ? Item.Type.CARD : Item.Type.PACK;
                    packContents.add(new Item(type, 1, selection));
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

            if (count == 1)
                _counts.remove(packId);
            else
                _counts.put(packId, count - 1);

            return packCollection;
        }
        return null;
    }

    @Override
    public Map<String, Integer> getAll() {
        return Collections.unmodifiableMap(_counts);
    }

    @Override
    public int getItemCount(String blueprintId) {
        Integer count = _counts.get(blueprintId);
        if (count == null)
            return 0;
        return count;
    }

    @Override
    public List<Item> getAllItems() {
        List<Item> result = new ArrayList<Item>();

        for (Map.Entry<String, Integer> itemCount : _counts.entrySet()) {
            String blueprintId = itemCount.getKey();
            int count = itemCount.getValue();
            if (!blueprintId.contains("_"))
                result.add(new Item(Item.Type.PACK, count, blueprintId));
            else
                result.add(new Item(Item.Type.CARD, count, blueprintId));
        }

        return result;
    }


}
