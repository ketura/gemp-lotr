package com.gempukku.lotro.cards.set2.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.PutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.logic.effects.RevealCardEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Ally • Home 3 • Man
 * Strength: 4
 * Vitality: 2
 * Site: 3
 * Game Text: To play, spot Gandalf. Fellowship: Exert Hugin to reveal a Free Peoples card from your discard pile
 * and place it beneath your draw deck.
 */
public class Card2_024 extends AbstractAlly {
    public Card2_024() {
        super(1, SitesBlock.FELLOWSHIP, 3, 4, 2, Race.MAN, Culture.GANDALF, "Hugin", "Emissary from Laketown", true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Filters.gandalf);
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseCardsFromDiscardEffect(playerId, 1, 1, Side.FREE_PEOPLE) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                            action.appendEffect(
                                    new RevealCardEffect(self, cards));
                            for (PhysicalCard card : cards) {
                                action.appendEffect(
                                        new PutCardFromDiscardOnBottomOfDeckEffect(card));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
