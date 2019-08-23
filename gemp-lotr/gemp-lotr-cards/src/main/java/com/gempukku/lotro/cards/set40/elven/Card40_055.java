package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;

/**
 * Title: Lorien Patrol
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event - Archery
 * Card Number: 1U55
 * Game Text: Exert an [ELVEN] ally and add a threat to wound a minion. If it is an [ISENGARD] minion, wound it again.
 */
public class Card40_055 extends AbstractEvent{
    public Card40_055() {
        super(Side.FREE_PEOPLE, 1, Culture.ELVEN, "Lorien Patrol", Phase.ARCHERY);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.ELVEN, CardType.ALLY)
                && PlayConditions.canAddThreat(game, self, 1);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ELVEN, CardType.ALLY));
        action.appendCost(
                new AddThreatsEffect(playerId, self, 1));
        action.appendEffect(
                new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION) {
                    @Override
                    protected void woundedCardsCallback(Collection<PhysicalCard> cards) {
                        if (cards.size()>0) {
                            PhysicalCard minion = cards.iterator().next();
                            if (minion.getBlueprint().getCulture() == Culture.ISENGARD) {
                                action.appendEffect(
                                        new WoundCharactersEffect(self, minion));
                            }
                        }
                    }
                });
        return action;
    }
}
