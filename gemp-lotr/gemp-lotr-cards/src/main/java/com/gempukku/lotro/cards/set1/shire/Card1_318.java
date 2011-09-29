package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
import com.gempukku.lotro.cards.costs.DiscardCardsFromPlayCost;
import com.gempukku.lotro.cards.effects.PlaySiteEffect;
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
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Possession
 * Game Text: Plays to your support area. Fellowship or Regroup: Exert 2 Hobbits and discard Thror's Map to play the
 * fellowship's next site (replacing opponent's site if necessary).
 */
public class Card1_318 extends AbstractPermanent {
    public Card1_318() {
        super(Side.FREE_PEOPLE, 0, CardType.POSSESSION, Culture.SHIRE, Zone.FREE_SUPPORT, "Thror's Map", true);
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if ((PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                || PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.REGROUP, self))
                && PlayConditions.canExertMultiple(self, game.getGameState(), game.getModifiersQuerying(), 1, 2, Filters.race(Race.HOBBIT))) {
            Keyword phaseKeyword = (game.getGameState().getCurrentPhase() == Phase.FELLOWSHIP) ? Keyword.FELLOWSHIP : Keyword.REGROUP;
            final ActivateCardAction action = new ActivateCardAction(self, phaseKeyword);
            action.appendCost(
                    new ChooseAndExertCharactersCost(action, playerId, 2, 2, Filters.race(Race.HOBBIT)));
            action.appendCost(new DiscardCardsFromPlayCost(self, self));
            action.appendEffect(new PlaySiteEffect(playerId, game.getGameState().getCurrentSiteNumber() + 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
