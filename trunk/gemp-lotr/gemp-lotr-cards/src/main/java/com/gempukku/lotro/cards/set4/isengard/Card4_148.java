package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each time a companion or ally loses a skirmish involving an Uruk-hai, place
 * an [ISENGARD] token on this card. Shadow: Remove 2 [ISENGARD] tokens from this card to take control of a site.
 */
public class Card4_148 extends AbstractPermanent {
    public Card4_148() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "Down to the Last Child", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.race(Race.URUK_HAI))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.ISENGARD));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 0)
                && game.getGameState().getTokenCount(self, Token.ISENGARD) >= 2) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTokenEffect(self, self, Token.ISENGARD, 2));
            action.appendEffect(
                    new TakeControlOfASiteEffect(self, playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
