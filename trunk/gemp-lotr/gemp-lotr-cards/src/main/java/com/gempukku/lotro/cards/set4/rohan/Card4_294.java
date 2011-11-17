package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Fellowship: Exert a [ROHAN] Man to play a hand weapon from your discard pile.
 */
public class Card4_294 extends AbstractPermanent {
    public Card4_294() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Weapon Store");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, Culture.ROHAN, Race.MAN)
                && PlayConditions.canPlayFromDiscard(playerId, game, PossessionClass.HAND_WEAPON)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ROHAN, Race.MAN));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, PossessionClass.HAND_WEAPON));
            return Collections.singletonList(action);
        }
        return null;
    }
}
