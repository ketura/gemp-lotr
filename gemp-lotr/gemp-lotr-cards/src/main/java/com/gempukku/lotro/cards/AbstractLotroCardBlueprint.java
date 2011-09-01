package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractLotroCardBlueprint implements LotroCardBlueprint {
    private String _name;
    private CardType _cardType;
    private Side _side;
    private Culture _culture;
    private boolean _unique;
    private Signet _signet;
    private Map<Keyword, Integer> _keywords = new HashMap<Keyword, Integer>();

    public AbstractLotroCardBlueprint(Side side, CardType cardType, Culture culture, String name) {
        this(side, cardType, culture, name, false);
    }

    public AbstractLotroCardBlueprint(Side side, CardType cardType, Culture culture, String name, boolean unique) {
        _side = side;
        _cardType = cardType;
        _culture = culture;
        _name = name;
        _unique = unique;
    }

    protected void addKeyword(Keyword keyword) {
        addKeyword(keyword, 1);
    }

    protected void addKeyword(Keyword keyword, int number) {
        _keywords.put(keyword, number);
    }

    @Override
    public boolean hasKeyword(Keyword keyword) {
        return _keywords.containsKey(keyword);
    }

    @Override
    public int getKeywordCount(Keyword keyword) {
        Integer count = _keywords.get(keyword);
        if (count == null)
            return 0;
        else
            return count;
    }

    @Override
    public Culture getCulture() {
        return _culture;
    }

    @Override
    public CardType getCardType() {
        return _cardType;
    }

    @Override
    public Side getSide() {
        return _side;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public boolean isUnique() {
        return _unique;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return null;
    }

    @Override
    public int getStrength() {
        throw new UnsupportedOperationException("This method should not be called on this card");
    }

    @Override
    public int getVitality() {
        throw new UnsupportedOperationException("This method should not be called on this card");
    }

    @Override
    public int getResistance() {
        throw new UnsupportedOperationException("This method should not be called on this card");
    }

    @Override
    public int getSiteNumber() {
        throw new UnsupportedOperationException("This method should not be called on this card");
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getRequiredBeforeTriggers(LotroGame game, Effect effect, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getOptionalBeforeTriggers(String playerId, LotroGame lotroGame, Effect effect, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public List<? extends Action> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    @Override
    public Signet getSignet() {
        return _signet;
    }

    @Override
    public Direction getSiteDirection() {
        throw new UnsupportedOperationException("This method should not be called on this card");
    }
}
