package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 3
 * Type: Site
 * Site: 4
 * Game Text: Underground. Shadow: Remove (1) to play a Shadow weapon from your discard pile.
 */
public class Card1_345 extends AbstractSite {
    public Card1_345() {
        super("Mithril Mine", Block.FELLOWSHIP, 4, 3, Direction.RIGHT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.SHADOW, self)
                && game.getGameState().getTwilightPool() >= 1
                && Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.race(Race.ORC), Filters.playable(game, 1)).size() > 0) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new RemoveTwilightEffect(1));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId),
                            Filters.and(
                                    Filters.side(Side.SHADOW),
                                    Filters.or(
                                            Filters.keyword(Keyword.HAND_WEAPON),
                                            Filters.keyword(Keyword.RANGED_WEAPON))
                            )));
            return Collections.singletonList(action);
        }
        return null;
    }
}
