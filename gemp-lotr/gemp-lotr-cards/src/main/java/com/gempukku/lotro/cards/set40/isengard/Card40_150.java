package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.saruman);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.saruman));
        int count = 0;
        for (PhysicalCard physicalCard : Filters.filterActive(game, Culture.ISENGARD, CardType.ARTIFACT)) {
            count += Filters.filter(game.getGameState().getStackedCards(physicalCard), game, Culture.ISENGARD, Keyword.SPELL).size();
        }

        int exertCount = Math.min(3, count);
        if (exertCount > 0)
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, GameUtils.getFreePeoplePlayer(game), exertCount, exertCount, CardType.COMPANION));
        return action;
    }
}
