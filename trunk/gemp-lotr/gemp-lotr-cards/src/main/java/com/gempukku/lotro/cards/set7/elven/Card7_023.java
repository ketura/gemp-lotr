package com.gempukku.lotro.cards.set7.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PutCardFromDeckOnBottomOfDeckEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: Regroup: Exert an Elf to reveal the top card of your draw deck. If it is an [ELVEN] card, remove
 * 3 threats. Place the revealed card beneath your draw deck.
 */
public class Card7_023 extends AbstractPermanent {
    public Card7_023() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Into the West");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canExert(self, game, Race.ELF)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF));
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> cards) {
                            for (PhysicalCard card : cards) {
                                if (card.getBlueprint().getCulture() == Culture.ELVEN) {
                                    action.appendEffect(
                                            new RemoveThreatsEffect(self, 3));
                                }
                                action.appendEffect(
                                        new PutCardFromDeckOnBottomOfDeckEffect(self, card));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
