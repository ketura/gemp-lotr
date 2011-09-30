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

public class WoundCharacterEffect extends AbstractPreventableCardEffect {
    private Collection<PhysicalCard> _sources;

    public WoundCharacterEffect(Collection<PhysicalCard> sources, PhysicalCard... cards) {
        super(cards);
        _sources = sources;
    }

    public WoundCharacterEffect(Collection<PhysicalCard> sources, Filter filter) {
        super(filter);
        _sources = sources;
    }

    public WoundCharacterEffect(PhysicalCard source, PhysicalCard... cards) {
        super(cards);
        _sources = Collections.singleton(source);
    }

    public WoundCharacterEffect(PhysicalCard source, Filter filter) {
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
        Collection<PhysicalCard> cards = getCardsToBeAffected(game);
        return "Wound - " + getAppendedNames(cards);
    }

    @Override
    public EffectResult[] playEffect(LotroGame game) {
        Collection<PhysicalCard> cardsToWound = getCardsToBeAffected(game);

        for (PhysicalCard woundedCard : cardsToWound) {
            if (_sources != null)
                game.getGameState().sendMessage(woundedCard.getBlueprint().getName() + " is wounded by - " + getAppendedNames(_sources));
            else
                game.getGameState().sendMessage(woundedCard.getBlueprint().getName() + " is wounded");
            game.getGameState().addWound(woundedCard);
        }

        return new EffectResult[]{new WoundResult(cardsToWound)};
    }
}
