package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddTokenEffect;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.IncrementPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Response: If your [SAURON] minion wins a skirmish, place a [SAURON] token here (limit 1 per site).
 * Regroup: Add (2) for each token here. Discard this condition.
 */
public class Card7_314 extends AbstractPermanent {
    public Card7_314() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, "Stronghold of Cirith Ungol", null, true);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.owner(playerId), Culture.SAURON, CardType.MINION)
                && PlayConditions.checkPhaseLimit(game, self, Phase.REGROUP, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new IncrementPhaseLimitEffect(self, Phase.REGROUP, 1));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.SAURON));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)) {
            ActivateCardAction action = new ActivateCardAction(self);
            int tokens = game.getGameState().getTokenCount(self, Token.SAURON);
            action.appendEffect(
                    new AddTwilightEffect(self, 2 * tokens));
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
