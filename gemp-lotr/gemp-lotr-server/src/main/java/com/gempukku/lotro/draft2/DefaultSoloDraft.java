package com.gempukku.lotro.draft2;

import com.gempukku.lotro.draft2.builder.CardCollectionProducer;
import com.gempukku.lotro.draft2.builder.DraftPoolProducer;
import com.gempukku.lotro.game.CardCollection;
import com.gempukku.lotro.game.DefaultCardCollection;

import java.util.List;

public class DefaultSoloDraft implements SoloDraft {
    private String _format;
    private CardCollectionProducer _newCollection;
    private DraftPoolProducer _draftPool;
    private List<DraftChoiceDefinition> _draftChoiceDefinitions;

    public DefaultSoloDraft(String format, CardCollectionProducer newCollection, List<DraftChoiceDefinition> draftChoiceDefinitions, DraftPoolProducer draftPool) {
        _format = format;
        _newCollection = newCollection;
        _draftPool = draftPool;
        _draftChoiceDefinitions = draftChoiceDefinitions;
    }

    @Override
    public CardCollection initializeNewCollection(long seed) {
        return (_newCollection != null) ? _newCollection.getCardCollection(seed) : null;
    }

    @Override
    public List<String> initializeDraftPool(long seed, long code) {
        return (_draftPool != null) ? _draftPool.getDraftPool(seed, code) : null;
    }

    @Override
    public Iterable<DraftChoice> getAvailableChoices(long seed, int stage, DefaultCardCollection draftPool) {
        return _draftChoiceDefinitions.get(stage).getDraftChoice(seed, stage, draftPool);
    }

    @Override
    public CardCollection getCardsForChoiceId(String choiceId, long seed, int stage) {
        return _draftChoiceDefinitions.get(stage).getCardsForChoiceId(choiceId, seed, stage);
    }

    @Override
    public boolean hasNextStage(long seed, int stage) {
        return stage + 1 < _draftChoiceDefinitions.size();
    }

    @Override
    public String getFormat() {
        return _format;
    }
}
