package com.gempukku.lotro.cards.set6.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Set: Ents of Fangorn
 * Twilight Cost: 3
 * Type: Site
 * Site: 6T
 * Game Text: Sanctuary. Fellowship: Spot 3 companions with the Theoden signet and discard your hand to draw 4 cards.
 */
public class Card6_118 extends AbstractSite {
    public Card6_118() {
        super("Hornburg Hall", Block.TWO_TOWERS, 6, 3, Direction.LEFT);

    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canSpot(game, 3, CardType.COMPANION, Signet.THÉODEN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            Set<PhysicalCard> hand = new HashSet<PhysicalCard>(game.getGameState().getHand(playerId));
            action.appendCost(
                    new DiscardCardsFromHandEffect(self, playerId, hand, false));
            action.appendEffect(
                    new DrawCardEffect(playerId, 4));
            return Collections.singletonList(action);
        }
        return null;
    }
}
