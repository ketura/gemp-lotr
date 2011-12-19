package com.gempukku.lotro.cards.set12.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Black Rider
 * Twilight Cost: 0
 * Type: Site
 * Game Text: Forest. Shadow: Play a Nazgul (or spot 6 companions and discard 2 cards from hand) to draw a card.
 */
public class Card12_185 extends AbstractNewSite {
    public Card12_185() {
        super("The Angle", 0, Direction.LEFT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.SHADOW, self)
                && (
                PlayConditions.canPlayFromHand(playerId, game, Race.NAZGUL)
                        || (PlayConditions.canSpot(game, 6, CardType.COMPANION) && PlayConditions.canDiscardFromHand(game, playerId, 2, Filters.any)))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, Race.NAZGUL) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play a Nazgul";
                        }
                    });
            if (PlayConditions.canSpot(game, 6, CardType.COMPANION))
                possibleCosts.add(
                        new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Discard 2 cards from hand";
                            }
                        });

            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new DrawCardsEffect(playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
