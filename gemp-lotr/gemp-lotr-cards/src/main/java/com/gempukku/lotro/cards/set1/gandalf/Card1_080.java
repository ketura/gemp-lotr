package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Ally • Home 3 • Man
 * Strength: 2
 * Vitality: 2
 * Site: 3
 * Game Text: To play, spot Gandalf. Fellowship: Exert Ottar to discard up to 3 cards from hand and draw an equal
 * number of cards.
 */
public class Card1_080 extends AbstractAlly {
    public Card1_080() {
        super(1, SitesBlock.FELLOWSHIP, 3, 2, 2, Race.MAN, Culture.GANDALF, "Ottar", "Man of Laketown", true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Filters.gandalf);
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseCardsFromHandEffect(playerId, 0, 3, Filters.any) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            action.appendEffect(new DiscardCardsFromHandEffect(self, playerId, selectedCards, false));
                            action.appendEffect(new DrawCardsEffect(action, playerId, selectedCards.size()));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
