package com.gempukku.lotro.cards.set30.shire;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CardPhaseLimitEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.ConstantEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.NegativeEvaluator;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

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
 * Game Text: Burglar. Each time Bilbo wins a skirmish, you may draw 3 cards. Skirmish: Discard 2 cards from hand to make Gollum strength -2 (limit -4).
 */
public class Card30_045 extends AbstractCompanion {
    public Card30_045() {
        super(0, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Bilbo", "Master in Riddles", true);
        addKeyword(Keyword.BURGLAR);
		addKeyword(Keyword.CAN_START_WITH_RING);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DrawCardsEffect(action, playerId, 3));
            return Collections.singletonList(action);
        }
        return null;
    }
	
	@Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromHand(game, playerId, 2, Filters.any)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose Gollum", Filters.name("Gollum")) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), null,
					    new NegativeEvaluator(new CardPhaseLimitEvaluator(
						    game, self, Phase.SKIRMISH, 4, new ConstantEvaluator(2))))));
                        }
	    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
