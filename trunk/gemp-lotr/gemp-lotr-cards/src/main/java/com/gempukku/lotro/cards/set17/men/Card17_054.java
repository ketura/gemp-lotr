package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 11
 * Vitality: 2
 * Site: 4
 * Game Text: Fierce. Maneuver: Discard this minion and a [MEN] minion with a twilight cost 2 or less from play to play
 * Stampeding Chief from your draw deck. It is twilight cost -8.
 */
public class Card17_054 extends AbstractMinion {
    public Card17_054() {
        super(5, 11, 2, 4, Race.MAN, Culture.MEN, "Stampeding Shepherd");
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSelfDiscard(self, game)
                && PlayConditions.canDiscardFromPlay(self, game, Culture.MEN, CardType.MINION, Filters.maxPrintedTwilightCost(2))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.MEN, CardType.MINION, Filters.maxPrintedTwilightCost(2)));
            action.appendEffect(
                    new ChooseAndPlayCardFromDeckEffect(playerId, -8, Filters.name("Stampeding Chief")));
            return Collections.singletonList(action);
        }
        return null;
    }
}
