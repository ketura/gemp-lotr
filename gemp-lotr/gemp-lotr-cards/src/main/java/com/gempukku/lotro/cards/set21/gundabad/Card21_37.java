package com.gempukku.lotro.cards.set21.gundabad;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExhaustCharacterEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardOnTopOfDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Shadow
 * Culture: Gundabad
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: If there is a [DWARVEN] companion in the dead pile, each [DWARVEN] companion comes into play exhausted.
 * Regroup: Place a Shadow card (except Smaug) from your discard pile on top of your draw deck. Discard this condition.
 */
public class Card21_37 extends AbstractPermanent {
    public Card21_37() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.GUNDABAD, Zone.SUPPORT, "Not at Home", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, CardType.COMPANION, Culture.DWARVEN)
                && Filters.filter(gameState.getDeadPile(gameState.getCurrentPlayerId()), gameState, modifiersQuerying, filters.and(CardType.COMPANION, Culture.DWARVEN)).size() > 0) {
            PlayCardResult playCardResult = (PlayCardResult) effectResult;
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExhaustCharacterEffect(self, action, playCardResult.getPlayedCard()));
            return Collections.singletonList(action);
        }
        return null;
    }
	
	@Override
	protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
		if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardOnTopOfDeckEffect(action, playerId, 1, 1, filters.not(name("Smaug"))));	
            action.appendEffect(
					new SelfDiscardEffect(self));
            return Collections.singletonList(action);
		}
		return null;
	}
}