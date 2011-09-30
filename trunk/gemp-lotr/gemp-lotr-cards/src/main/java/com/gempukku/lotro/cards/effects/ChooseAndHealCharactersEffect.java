package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardsEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collection;

public class ChooseAndHealCharactersEffect extends ChooseActiveCardsEffect {
    private CostToEffectAction _action;
    private String _playerId;

    public ChooseAndHealCharactersEffect(CostToEffectAction action, String playerId, Filter... filters) {
        this(action, playerId, 1, 1, filters);
    }

    public ChooseAndHealCharactersEffect(CostToEffectAction action, String playerId, int minimum, int maximum, Filter... filters) {
        super(playerId, "Choose character(s) to heal", minimum, maximum, Filters.and(
                filters, new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return modifiersQuerying.canBeHealed(gameState, physicalCard);
                    }
                }));
        _action = action;
        _playerId = playerId;
    }

    @Override
    public String getText(LotroGame game) {
        return "Choose character(s) to heal";
    }

    @Override
    protected void cardsSelected(Collection<PhysicalCard> cards) {
        if (_action.getActionSource() != null)
            _action.appendEffect(new CardAffectsCardEffect(_action.getActionSource(), cards));
        _action.appendEffect(new HealCharacterEffect(_playerId, Filters.in(cards)));
    }
}
