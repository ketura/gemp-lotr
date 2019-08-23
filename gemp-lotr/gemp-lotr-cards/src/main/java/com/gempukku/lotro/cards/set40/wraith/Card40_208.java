package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.FpSkirmishResistanceStrengthOverrideModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Ulaire Nertea, Morgul Fiend
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 4
 * Type: Minion - Nazgul
 * Strength: 9
 * Vitality: 2
 * Home: 3
 * Card Number: 1U208
 * Game Text: Skirmish: Exert Ulaire Nertea at a forest site to make a character he is skirmishing use resistance
 * instead of strength to resolve this skirmish.
 */
public class Card40_208 extends AbstractMinion {
    public Card40_208() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, Names.nertea, "Morgul Fiend", true);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.location(game, Keyword.FOREST)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a character", Filters.character, Filters.inSkirmishAgainst(self)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new FpSkirmishResistanceStrengthOverrideModifier(self, card, null)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
