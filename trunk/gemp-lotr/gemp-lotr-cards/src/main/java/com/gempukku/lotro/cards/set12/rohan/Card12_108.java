package com.gempukku.lotro.cards.set12.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 4
 * Type: Condition • Support Area
 * Game Text: Toil 2. (For each [ROHAN] character you exert when playing this, its twilight cost is -2) Maneuver: Spot
 * a [ROHAN] Man and discard this condition to exert a minion. Maneuver: Spot a mounted [ROHAN] Man and discard this
 * condition to return an exhausted minion to its owner's hand.
 */
public class Card12_108 extends AbstractPermanent {
    public Card12_108() {
        super(Side.FREE_PEOPLE, 4, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Cast Out");
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            List<ActivateCardAction> actions = new LinkedList<ActivateCardAction>();
            if (PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Exert a minion");
                action.appendCost(
                        new SelfDiscardEffect(self));
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.MINION));
                actions.add(action);
            }
            if (PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN, Filters.mounted)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Return an exhausted minion to hand");
                action.appendCost(
                        new SelfDiscardEffect(self));
                action.appendEffect(
                        new ChooseAndReturnCardsToHandEffect(action, playerId, 1, 1, CardType.MINION, Filters.exhausted));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
