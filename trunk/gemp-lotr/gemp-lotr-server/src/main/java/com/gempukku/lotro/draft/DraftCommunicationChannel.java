package com.gempukku.lotro.draft;

import com.gempukku.lotro.game.CardCollection;
import com.gempukku.polling.LongPollableResource;
import com.gempukku.polling.LongPollingResource;

public class DraftCommunicationChannel implements LongPollableResource {
    private int _channelNumber;
    private long _lastAccessed;

    private String _cardChoiceOnClient;
    private LongPollingResource _longPollingResource;

    public DraftCommunicationChannel(int channelNumber) {
        _channelNumber = channelNumber;
    }

    public int getChannelNumber() {
        return _channelNumber;
    }

    public long getLastAccessed() {
        return _lastAccessed;
    }

    private void updateLastAccess() {
        _lastAccessed = System.currentTimeMillis();
    }

    @Override
    public void deregisterResource(LongPollingResource longPollingResource) {
        _longPollingResource = null;
    }

    @Override
    public void registerForChanges(LongPollingResource longPollingResource) {
        _longPollingResource = longPollingResource;
    }

    public synchronized void draftChanged() {
        if (_longPollingResource != null)
            _longPollingResource.processIfNotProcessed();
    }

    public boolean hasChangesInCommunicationChannel(DraftCardChoice draftCardChoice) {
        updateLastAccess();

        CardCollection cardCollection = draftCardChoice.getCardCollection();
        if (cardCollection == null)
            return _cardChoiceOnClient != null;
        return  !getSerialized(cardCollection).equals(_cardChoiceOnClient);
    }

    private String getSerialized(CardCollection cardCollection) {
        StringBuilder sb = new StringBuilder();
        for (CardCollection.Item collectionItem : cardCollection.getAll().values())
            sb.append(collectionItem.getCount()+"x"+collectionItem.getBlueprintId()+"|");

        return sb.toString();
    }

    public void processCommunicationChannel(DraftCardChoice draftCardChoice, CardCollection chosenCards, DraftChannelVisitor draftChannelVisitor) {
        updateLastAccess();

        CardCollection cardCollection = draftCardChoice.getCardCollection();
        if (cardCollection != null) {
            draftChannelVisitor.timeLeft(draftCardChoice.getTimeLeft());
            draftChannelVisitor.cardChoice(cardCollection);
            _cardChoiceOnClient = getSerialized(cardCollection);
        } else {
            draftChannelVisitor.noCardChoice();
            _cardChoiceOnClient = null;
        }
        draftChannelVisitor.chosenCards(chosenCards);
    }
}
