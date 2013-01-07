package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Celeborn, Lord of the Galadhrim
 * Elven * Ally • Elf • Lothlorien
 * 6	3
 * Manuever: Exert Celeborn to reveal the top card of your draw deck. If it is an [Elven] card, you may wound a minion.
 */
public class Card20_077 extends AbstractAlly {
    public Card20_077() {
        super(2, null, 0, 6, 3, Race.ELF, Culture.ELVEN, "Celeborn", "Lord of the Galadhrim", true);
        addKeyword(Keyword.LOTHLORIEN);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSelfExert(self, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                            for (PhysicalCard revealedCard : revealedCards) {
                                if (Filters.and(Culture.ELVEN).accepts(game.getGameState(), game.getModifiersQuerying(), revealedCard))
                                    action.appendEffect(
                                            new ChooseAndWoundCharactersEffect(action, playerId, 0, 1, CardType.MINION));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
