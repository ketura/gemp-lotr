package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

public class HealByDiscardRule {
    private ModifiersLogic modifiersLogic;

    public HealByDiscardRule(ModifiersLogic modifiersLogic) {
        this.modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        modifiersLogic.addAlwaysOnModifier(
                new AbstractModifier(null, "Heal by discarding", Zone.HAND, ModifierEffect.EXTRA_ACTION_MODIFIER) {
                    @Override
                    public List<? extends ActivateCardAction> getExtraPhaseAction(GameState gameState, ModifiersQuerying modifiersQuerying, final PhysicalCard card) {
                        if (PlayConditions.canHealByDiscarding(gameState, modifiersQuerying, card)) {
                            ActivateCardAction action = new ActivateCardAction(card);
                            action.setText("Heal by discarding");
                            action.appendCost(new DiscardCardsFromHandEffect(null, card.getOwner(), Collections.singleton(card), false));
                            action.appendCost(
                                    new UnrespondableEffect() {
                                        @Override
                                        protected void doPlayEffect(LotroGame game) {
                                            game.getGameState().eventPlayed(card);
                                        }
                                    });

                            PhysicalCard active = Filters.findFirstActive(gameState, modifiersQuerying, Filters.name(card.getBlueprint().getName()));
                            if (active != null)
                                action.appendEffect(new HealCharactersEffect(card, active));

                            return Collections.singletonList(action);
                        }

                        return super.getExtraPhaseAction(gameState, modifiersQuerying, card);
                    }
                });
    }
}
