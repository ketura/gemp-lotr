package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
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
 * Type: Site
 * Site: 1
 * Game Text: Fellowship: Exert a Hobbit to play a companion or ally; that character's twilight cost is -1.
 */
public class Card1_326 extends AbstractSite {
    public Card1_326() {
        super("Westfarthing", Block.FELLOWSHIP, 1, 0, Direction.LEFT);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.HOBBIT))
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY)), Filters.playable(game, -1)).size() > 0) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.HOBBIT)));
            action.appendEffect(
                    new ChooseAndPlayCardFromHandEffect(playerId, game.getGameState().getHand(playerId), Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY)), -1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
