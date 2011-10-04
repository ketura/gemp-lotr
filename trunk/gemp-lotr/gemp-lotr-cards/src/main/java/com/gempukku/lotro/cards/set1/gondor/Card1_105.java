package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
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
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Maneuver: Exert a [GONDOR] character to wound a [SAURON] minion.
 */
public class Card1_105 extends AbstractPermanent {
    public Card1_105() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, Zone.FREE_SUPPORT, "Foes of Mordor");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.GONDOR), Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY)))) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.MANEUVER);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.GONDOR), Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY))));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.SAURON), Filters.type(CardType.MINION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
