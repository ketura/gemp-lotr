package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 6
 * Game Text: Maneuver: Remove (3) to exert a Hobbit (except the Ring-bearer).
 */
public class Card1_257 extends AbstractMinion {
    public Card1_257() {
        super(3, 9, 3, 6, Race.ORC, Culture.SAURON, "Morgul Skirmisher");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.MANEUVER, self, 3)) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.MANEUVER, "Remove (3) to exert a Hobbit (except the Ring-bearer).");
            action.appendCost(new RemoveTwilightEffect(3));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.HOBBIT), Filters.not(Filters.keyword(Keyword.RING_BEARER))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
