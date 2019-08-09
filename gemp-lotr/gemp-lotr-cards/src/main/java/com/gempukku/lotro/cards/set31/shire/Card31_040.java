package com.gempukku.lotro.cards.set31.shire;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;

/**
 * Barrels [Shire]
 Event • Regroup
 Twilight Cost 1
 'Exert 2 [Dwarven] companions to discard up to 2 minions (except Smaug) and draw 3 cards. If the fellowship is at
 a river, make the move limit +1 for this turn.'
 */
public class Card31_040 extends AbstractEvent {
    public Card31_040() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "Barrels", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, 1, 2, Culture.DWARVEN, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Culture.DWARVEN, CardType.COMPANION));
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 0, 2, CardType.MINION, Filters.not(Filters.name("Smaug"))));
        action.appendEffect(
                new DrawCardsEffect(action, playerId, 3));
        if (game.getModifiersQuerying().hasKeyword(game, game.getGameState().getCurrentSite(), Keyword.RIVER)) {
            action.appendEffect(
                    new AddUntilEndOfTurnModifierEffect(
                            new MoveLimitModifier(self, 1)));
        }
        return action;
    }
}
