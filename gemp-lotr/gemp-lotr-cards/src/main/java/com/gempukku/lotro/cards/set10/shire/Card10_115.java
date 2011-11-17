package com.gempukku.lotro.cards.set10.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Stealth. If there are 6 or more [SHIRE] cards in your discard pile, cancel a skirmish involving your
 * Hobbit and a minion who is not fierce. Each minion in that skirmish is fierce until the regroup phase.
 */
public class Card10_115 extends AbstractEvent {
    public Card10_115() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "Slunk Out of Sight", Phase.SKIRMISH);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Culture.SHIRE).size() >= 6;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        Skirmish skirmish = game.getGameState().getSkirmish();
        if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.inSkirmish, Filters.owner(playerId), Race.HOBBIT)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.inSkirmish, CardType.MINION, Filters.not(Keyword.FIERCE)))
            action.appendEffect(
                    new CancelSkirmishEffect());
        action.appendEffect(
                new AddUntilStartOfPhaseModifierEffect(
                        new KeywordModifier(self, Filters.in(skirmish.getShadowCharacters()), Keyword.FIERCE), Phase.REGROUP));
        return action;
    }
}
