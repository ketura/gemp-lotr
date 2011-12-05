package com.gempukku.lotro.cards.set11.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Event • Skirmish
 * Game Text: Stealth. Cancel a skirmish involving a Hobbit and a minion whose strength is lower than that Hobbit's
 * resistance.
 */
public class Card11_162 extends AbstractEvent {
    public Card11_162() {
        super(Side.FREE_PEOPLE, 2, Culture.SHIRE, "Crouched Down", Phase.SKIRMISH);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new CancelSkirmishEffect(Race.HOBBIT,
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                int resistance = modifiersQuerying.getResistance(gameState, physicalCard);
                                return Filters.inSkirmishAgainst(CardType.MINION, Filters.lessStrengthThan(resistance)).accepts(gameState, modifiersQuerying, physicalCard);
                            }
                        }));
        return action;
    }
}
