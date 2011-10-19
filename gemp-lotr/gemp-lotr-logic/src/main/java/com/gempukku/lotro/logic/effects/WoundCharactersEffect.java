package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.Collection;
import java.util.Collections;

public class WoundCharactersEffect extends AbstractPreventableCardEffect {
    private Collection<PhysicalCard> _sources;

    public WoundCharactersEffect(Collection<PhysicalCard> sources, Filterable... filter) {
        super(filter);
        _sources = sources;
    }

    public WoundCharactersEffect(PhysicalCard source, PhysicalCard... cards) {
        super(cards);
        if (source != null)
            _sources = Collections.singleton(source);
    }

    public WoundCharactersEffect(PhysicalCard source, Filterable... filter) {
        super(filter);
        if (source != null)
            _sources = Collections.singleton(source);
    }

    public Collection<PhysicalCard> getSources() {
        return _sources;
    }

    @Override
    protected Filter getExtraAffectableFilter() {
        return new Filter() {
            @Override
            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                return modifiersQuerying.canTakeWound(gameState, physicalCard);
            }
        };
    }

    @Override
    public EffectResult.Type getType() {
        return EffectResult.Type.WOUND;
    }

    @Override
    public String getText(LotroGame game) {
        Collection<PhysicalCard> cards = getAffectedCardsMinusPrevented(game);
        return "Wound - " + getAppendedTextNames(cards);
    }

    @Override
    protected EffectResult[] playoutEffectOn(LotroGame game, Collection<PhysicalCard> cards) {
        if (cards.size() > 0)
            if (_sources != null)
                game.getGameState().sendMessage(getAppendedNames(cards) + " " + GameUtils.be(cards) + " wounded by - " + getAppendedNames(_sources));
            else
                game.getGameState().sendMessage(getAppendedNames(cards) + " " + GameUtils.be(cards) + " wounded");

        for (PhysicalCard woundedCard : cards) {
            game.getGameState().addWound(woundedCard);
        }

        return new EffectResult[]{new WoundResult(cards)};
    }
}
