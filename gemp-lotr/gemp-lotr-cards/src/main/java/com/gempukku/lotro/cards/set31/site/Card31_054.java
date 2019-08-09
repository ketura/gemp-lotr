package com.gempukku.lotro.cards.set31.site;

import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndRemoveFromTheGameCardsInPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Twilight Cost: 8
 * Type: Site
 * Site: 9
 * Game Text: Mountain. Maneuver: Remove your minion from the game to play a minion from your discard
 * pile. Its twilight cost is -2.
 */
public class Card31_054 extends AbstractSite {
    public Card31_054() {
        super("Ravenhill", Block.HOBBIT, 9, 8, Direction.RIGHT);
        addKeyword(Keyword.MOUNTAIN);
    }

    
    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSpot(game, CardType.MINION, Filters.owner(playerId))
                && PlayConditions.canPlayFromDiscard(playerId, game, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
					new ChooseAndRemoveFromTheGameCardsInPlayEffect(action, playerId, 1, 1, CardType.MINION));
            action.appendEffect(
					new ChooseAndPlayCardFromDiscardEffect(playerId, game, -2, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}