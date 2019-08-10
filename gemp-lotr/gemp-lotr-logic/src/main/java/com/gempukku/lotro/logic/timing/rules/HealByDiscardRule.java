package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;
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
                new AbstractModifier(null, "Heal by discarding", Zone.HAND,
                        new PhaseCondition(Phase.FELLOWSHIP), ModifierEffect.EXTRA_ACTION_MODIFIER) {
                    @Override
                    public List<? extends ActivateCardAction> getExtraPhaseAction(LotroGame game, final PhysicalCard card) {
                        if (canHealByDiscarding(game, card)) {
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

                            PhysicalCard active = Filters.findFirstActive(game, Filters.name(card.getBlueprint().getName()));
                            if (active != null)
                                action.appendEffect(new HealCharactersEffect(card, active));

                            return Collections.singletonList(action);
                        }

                        return null;
                    }
                });
    }

    private static boolean canHealByDiscarding(LotroGame game, PhysicalCard self) {
        LotroCardBlueprint blueprint = self.getBlueprint();
        if ((blueprint.getCardType() == CardType.COMPANION || blueprint.getCardType() == CardType.ALLY)
                && blueprint.isUnique()) {
            PhysicalCard matchingName = Filters.findFirstActive(game, Filters.name(blueprint.getName()));
            if (matchingName != null)
                return game.getGameState().getWounds(matchingName) > 0;
        }
        return false;
    }
}
