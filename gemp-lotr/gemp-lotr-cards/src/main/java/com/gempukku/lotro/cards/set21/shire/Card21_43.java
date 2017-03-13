package com.gempukku.lotro.cards.set21.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 8
 * Game Text: Each time Bilbo wins a skirmish, you may draw 3 cards and then discard 2 cards from hand.
 * Skirmish: Add 2 doubts to cancel a skirmish involving Gollum.
 */
public class Card21_43 extends AbstractCompanion {
    public Card21_43() {
        super(0, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Bilbo", "Expert Burglar", true);
		addKeyword(keyword.BURGLAR);
		addKeyword(Keyword.CAN_START_WITH_RING);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 3));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, 2, Filters.any));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddBurdenEffect(self.getOwner(), self, 2));
            action.appendEffect(
				if (game, Filters.and(self, Filters.name("Gollum")), Filters.inSkirmish) {
					new CancelSkirmishEffect(Filters.sameCard(self)));
				}
            return Collections.singletonList(action);
        }
        return null;
    }
}
