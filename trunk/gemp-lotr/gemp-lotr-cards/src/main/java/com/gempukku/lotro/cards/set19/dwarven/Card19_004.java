package com.gempukku.lotro.cards.set19.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;

/**
 * Set: Ages End
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Event • Regroup
 * Game Text: Spot Gimli and exert a fellowship companion to have each Shadow player discard one of his or her Shadow
 * cards from play.
 */
public class Card19_004 extends AbstractEvent {
    public Card19_004() {
        super(Side.FREE_PEOPLE, 2, Culture.DWARVEN, "That's Two!", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Filters.gimli)
                && PlayConditions.canExert(self, game, CardType.COMPANION, Keyword.FELLOWSHIP);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Keyword.FELLOWSHIP));
        for (String opponentId : GameUtils.getOpponents(game, playerId)) {
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, opponentId, 1, 1, Filters.owner(playerId), Side.SHADOW));
        }
        return action;
    }
}
