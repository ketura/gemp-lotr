package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;

/**
 * Title: Voice of Saruman
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Event - Maneuver
 * Card Number: 1R150
 * Game Text: Spell. Exert Saruman to make the Free Peoples player exert a companion for each [ISENGARD] spell stacked
 * on an [ISENGARD] artifact (limit 3).
 */
public class Card40_150 extends AbstractEvent {
    public Card40_150() {
        super(Side.SHADOW, 1, Culture.ISENGARD, "Voice of Saruman", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Filters.saruman);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.saruman));
        int count = 0;
        for (PhysicalCard physicalCard : Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Culture.ISENGARD, CardType.ARTIFACT)) {
            count += Filters.filter(game.getGameState().getStackedCards(physicalCard), game.getGameState(), game.getModifiersQuerying(), Culture.ISENGARD, Keyword.SPELL).size();
        }

        int exertCount = Math.min(3, count);
        if (exertCount > 0)
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, GameUtils.getFreePeoplePlayer(game), exertCount, exertCount, CardType.COMPANION));
        return action;
    }
}
