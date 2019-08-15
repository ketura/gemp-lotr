package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Response: If your [WRAITH] minion wins a skirmish, place a [WRAITH] token here (limit 1 per site).
 * Regroup: Spot 3 [WRAITH] tokens here to exhaust the Ring-bearer. Discard this condition.
 */
public class Card7_206 extends AbstractPermanent {
    public Card7_206() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, "Stronghold of Minas Morgul", null, true);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.owner(playerId), Culture.WRAITH, CardType.MINION)
        && PlayConditions.checkPhaseLimit(game, self, Phase.REGROUP, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new IncrementPhaseLimitEffect(self, Phase.REGROUP, 1));
            action.appendEffect(
                            new AddTokenEffect(self, self, Token.WRAITH));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canSpot(game, self, Filters.hasToken(Token.WRAITH, 3))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ExhaustCharacterEffect(self, action, Filters.ringBearer));
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
