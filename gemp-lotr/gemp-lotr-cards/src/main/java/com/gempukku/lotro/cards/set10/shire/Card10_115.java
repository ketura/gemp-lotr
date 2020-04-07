package com.gempukku.lotro.cards.set10.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.filter(game.getGameState().getDiscard(self.getOwner()), game, Culture.SHIRE).size() >= 6;
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        Skirmish skirmish = game.getGameState().getSkirmish();
        if (Filters.canSpot(game, Filters.inSkirmish, Filters.owner(playerId), Race.HOBBIT)
                && Filters.canSpot(game, Filters.inSkirmish, CardType.MINION, Filters.not(Keyword.FIERCE)))
            action.appendEffect(
                    new CancelSkirmishEffect());
        action.appendEffect(
                new AddUntilStartOfPhaseModifierEffect(
                        new KeywordModifier(self, Filters.in(skirmish.getShadowCharacters()), Keyword.FIERCE), Phase.REGROUP));
        return action;
    }
}
