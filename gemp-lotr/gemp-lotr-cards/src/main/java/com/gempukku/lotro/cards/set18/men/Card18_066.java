package com.gempukku.lotro.cards.set18.men;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardOnTopOfDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Possession • Support Area
 * Game Text: When you play this possession, you may add a [MEN] token here. Regroup: Remove 4 [MEN] tokens from here
 * to place a [MEN] card from your discard pile on the top of your draw deck.
 */
public class Card18_066 extends AbstractPermanent {
    public Card18_066() {
        super(Side.SHADOW, 3, CardType.POSSESSION, Culture.MEN, Zone.SUPPORT, "Fleet of Corsair Ships");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.MEN));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canRemoveTokens(game, self, Token.MEN, 4)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTokenEffect(self, self, Token.MEN, 4));
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardOnTopOfDeckEffect(action, playerId, 1, 1, Culture.MEN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
