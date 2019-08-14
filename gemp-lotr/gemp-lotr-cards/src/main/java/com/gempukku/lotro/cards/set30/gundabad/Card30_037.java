package com.gempukku.lotro.cards.set30.gundabad;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ExhaustCharacterEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDiscardOnTopOfDeckEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
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
public class Card30_037 extends AbstractPermanent {
    public Card30_037() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.GUNDABAD, "Not at Home", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
		if (TriggerConditions.played(game, effectResult, CardType.COMPANION, Culture.DWARVEN)
                && Filters.filter(game.getGameState().getDeadPile(game.getGameState().getCurrentPlayerId()), game, Filters.and(CardType.COMPANION, Culture.DWARVEN)).size() > 0) {
            PlayCardResult playCardResult = (PlayCardResult) effectResult;
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExhaustCharacterEffect(self, action, playCardResult.getPlayedCard()));
            return Collections.singletonList(action);
        }
        return null;
    }
	
	@Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
		if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardOnTopOfDeckEffect(action, playerId, 1, 1, Side.SHADOW, Filters.not(Filters.name("Smaug"))));
            action.appendEffect(
					new SelfDiscardEffect(self));
            return Collections.singletonList(action);
		}
		return null;
	}
}