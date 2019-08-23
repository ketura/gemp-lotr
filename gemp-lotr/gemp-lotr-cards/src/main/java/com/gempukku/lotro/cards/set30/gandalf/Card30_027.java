package com.gempukku.lotro.cards.set30.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
 
 /**
 * Set: Main Deck
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Companion • Wizard
 * Strength: 7
 * Vitality: 4
 * Resistance: 6
 * Game Text: Wise. Each time you play a [GANDALF] event, you may wound a minion. Fellowship: Add 2 doubts to play an
 * artifact or a Dwarf companion from your draw deck or discard pile
 */
public class Card30_027 extends AbstractCompanion {
    public Card30_027() {
        super(2, 7, 4, 6, Culture.GANDALF, Race.WIZARD, null, "Gandalf", "The Grey", true);
		addKeyword(Keyword.WISE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Culture.GANDALF, CardType.EVENT)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.insertEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
		return null;
	}
	
	@Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, PhysicalCard self) {
		if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
			action.appendCost(
                    new AddBurdenEffect(self.getOwner(), self, 2));
			List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(playerId, Filters.or(Filters.and(CardType.COMPANION, Race.DWARF), CardType.ARTIFACT)) {
                @Override
                public String getText(LotroGame game) {
                    return "Play an artifact or Dwarf companion from your draw deck";
                }
            });
            if (PlayConditions.canPlayFromDiscard(playerId, game, Filters.or(Filters.and(CardType.COMPANION, Race.DWARF), CardType.ARTIFACT))) {
                possibleEffects.add(
                        new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.or(Filters.and(CardType.COMPANION, Race.DWARF), CardType.ARTIFACT)) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play an artifact or Dwarf companion from your discard pile";
                    }
                });
            }
            action.appendEffect(
					new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
