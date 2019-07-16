package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.YesNoDecision;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Celeborn, Lord of the Golden Wood
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Ally - Elf - Lothlorien
 * Strength: 6
 * Vitality: 3
 * Card Number: 1R38
 * Game Text: Maneuver: Exert Celeborn to reveal the top card of your draw deck. If it is an [ELVEN] card,
 * you may discard it to wound a minion.
 */
public class Card40_038 extends AbstractAlly{
    public Card40_038() {
        super(2, Block.SECOND_ED, 0, 6, 3, Race.ELF, Culture.ELVEN, "Celeborn",
                "Lord of the Golden Wood", true);
        addKeyword(Keyword.LOTHLORIEN);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                            if (revealedCards.size()>0) {
                                PhysicalCard revealedCard = revealedCards.get(0);
                                if (Filters.and(Culture.ELVEN).accepts(game.getGameState(), game.getModifiersQuerying(), revealedCard)) {
                                    action.appendEffect(
                                            new PlayoutDecisionEffect(playerId, new YesNoDecision("Would you like to discard the card") {
                                                @Override
                                                protected void yes() {
                                                    action.appendEffect(
                                                            new DiscardTopCardFromDeckEffect(self, playerId, false));
                                                    action.appendEffect(
                                                            new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
                                                }
                                            }));
                                }
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
