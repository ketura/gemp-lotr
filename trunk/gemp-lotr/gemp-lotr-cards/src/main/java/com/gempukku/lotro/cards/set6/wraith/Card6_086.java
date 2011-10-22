package com.gempukku.lotro.cards.set6.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 2
 * Site: 3
 * Game Text: Regroup: Exert Ulaire Lemenya and discard 3 cards from hand to return a companion (except the Ring-bearer)
 * to owner's hand.
 */
public class Card6_086 extends AbstractMinion {
    public Card6_086() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, "Ulaire Lemenya", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.REGROUP, self, 0)
                && PlayConditions.canDiscardFromHand(game, playerId, 3, Filters.any)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 3));
            action.appendEffect(
                    new ChooseAndReturnCardsToHandEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Keyword.RING_BEARER)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
