package com.gempukku.lotro.cards.build;

import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class DefaultActionContext implements ActionContext {
    private Multimap<String, PhysicalCard> cardMemory = HashMultimap.create();
    private Map<String, String> valueMemory = new HashMap<>();

    private String performingPlayer;
    private LotroGame game;
    private PhysicalCard source;
    private EffectResult effectResult;
    private Effect effect;

    public DefaultActionContext(String performingPlayer, LotroGame game, PhysicalCard source, EffectResult effectResult, Effect effect) {
        this.performingPlayer = performingPlayer;
        this.game = game;
        this.source = source;
        this.effectResult = effectResult;
        this.effect = effect;
    }

    @Override
    public void setValueToMemory(String memory, String value) {
        valueMemory.put(memory, value);
    }

    @Override
    public String getValueFromMemory(String memory) {
        return valueMemory.get(memory);
    }

    @Override
    public void setCardMemory(String memory, PhysicalCard card) {
        cardMemory.removeAll(memory);
        cardMemory.put(memory, card);
    }

    @Override
    public void setCardMemory(String memory, Collection<? extends PhysicalCard> cards) {
        cardMemory.removeAll(memory);
        cardMemory.putAll(memory, cards);
    }

    @Override
    public Collection<? extends PhysicalCard> getCardsFromMemory(String memory) {
        return cardMemory.get(memory);
    }

    @Override
    public PhysicalCard getCardFromMemory(String memory) {
        final Collection<PhysicalCard> physicalCards = cardMemory.get(memory);
        if (physicalCards.size() != 1)
            throw new RuntimeException("Unable to retrieve one card from memory: " + memory);
        return physicalCards.iterator().next();
    }

    @Override
    public String getPerformingPlayer() {
        return performingPlayer;
    }

    @Override
    public LotroGame getGame() {
        return game;
    }

    @Override
    public PhysicalCard getSource() {
        return source;
    }

    @Override
    public EffectResult getEffectResult() {
        return effectResult;
    }

    @Override
    public Effect getEffect() {
        return effect;
    }
}
