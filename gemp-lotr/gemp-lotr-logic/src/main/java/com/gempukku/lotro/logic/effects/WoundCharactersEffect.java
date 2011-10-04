package com.gempukku.lotro.logic.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.Collection;
import java.util.Collections;

public class WoundCharactersEffect extends AbstractPreventableCardEffect {
    private Collection<PhysicalCard> _sources;

    public WoundCharactersEffect(Collection<PhysicalCard> sources, Filter filter) {
        super(filter);
        _sources = sources;
    }

    public WoundCharactersEffect(PhysicalCard source, PhysicalCard... cards) {
        super(cards);
        _sources = Collections.singleton(source);
    }

    public WoundCharactersEffect(PhysicalCard source, Filter filter) {
        super(filter);
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
        return "Wound - " + getAppendedNames(cards);
    }

    @Override
    protected EffectResult[] playoutEffectOn(LotroGame game, Collection<PhysicalCard> cards) {
        for (PhysicalCard woundedCard : cards) {
            if (_sources != null)
                game.getGameState().sendMessage(woundedCard.getBlueprint().getName() + " is wounded by - " + getAppendedNames(_sources));
            else
                game.getGameState().sendMessage(woundedCard.getBlueprint().getName() + " is wounded");
            game.getGameState().addWound(woundedCard);
        }

        return new EffectResult[]{new WoundResult(cards)};
    }
}
