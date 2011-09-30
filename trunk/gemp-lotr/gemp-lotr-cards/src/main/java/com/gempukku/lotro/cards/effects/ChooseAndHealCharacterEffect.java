package com.gempukku.lotro.cards.effects;

import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

public class ChooseAndHealCharacterEffect extends ChooseActiveCardEffect {
    private CostToEffectAction _action;
    private String _playerId;

    public ChooseAndHealCharacterEffect(CostToEffectAction action, String playerId, Filter... filters) {
        super(playerId, "Choose character to heal", Filters.and(
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
        return "Choose character to heal";
    }

    @Override
    protected void cardSelected(PhysicalCard character) {
        if (_action.getActionSource() != null)
            _action.appendEffect(new CardAffectsCardEffect(_action.getActionSource(), character));
        _action.appendEffect(new HealCharacterEffect(_playerId, character));
    }
}
