package com.gempukku.lotro.cards.set5.gollum;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.PutCardsFromDeckBeneathDrawDeckEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Companion
 * Strength: 3
 * Vitality: 4
 * Resistance: 6
 * Signet: Frodo
 * Game Text: Ring-bound. To play add a burden. Regroup: Exert Smeagol (or Gollum) twice to reveal the top 4 cards
 * of your draw deck. Wound a minion for each Shadow card revealed. Place those 4 cards beneath your draw deck in any order.
 */
public class Card5_028 extends AbstractCompanion {
    public Card5_028() {
        super(0, 3, 4, 6, Culture.GOLLUM, null, Signet.FRODO, "Smeagol", "Old Noser", true);
        addKeyword(Keyword.RING_BOUND);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayPermanentAction playCardAction = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        playCardAction.appendCost(
                new AddBurdenEffect(self, 1));
        return playCardAction;
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canExert(self, game, 2, Filters.gollumOrSmeagol)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.gollumOrSmeagol));
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, playerId, 4) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> cards) {
                            int shadowCards = Filters.filter(cards, game.getGameState(), game.getModifiersQuerying(), Side.SHADOW).size();
                            action.appendEffect(
                                    new PutCardsFromDeckBeneathDrawDeckEffect(action, self, playerId, cards));
                            for (int i = 0; i < shadowCards; i++)
                                action.appendEffect(
                                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
