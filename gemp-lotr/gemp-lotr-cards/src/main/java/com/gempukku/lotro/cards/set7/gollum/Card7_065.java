package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: To play, spot Smeagol. Regroup: Discard Smeagol to remove 2 threats.
 */
public class Card7_065 extends AbstractPermanent {
    public Card7_065() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.GOLLUM, "Never");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.smeagol);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canDiscardFromPlay(self, game, Filters.smeagol)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.smeagol));
            action.appendEffect(
                    new RemoveThreatsEffect(self, 2));
            return Collections.singletonList(action);
        }
        return null;
    }
}
