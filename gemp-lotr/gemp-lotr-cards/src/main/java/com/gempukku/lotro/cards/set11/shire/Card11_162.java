package com.gempukku.lotro.cards.set11.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;

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
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new CancelSkirmishEffect(Race.HOBBIT,
                        new Filter() {
                            @Override
                            public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                int resistance = game.getModifiersQuerying().getResistance(game, physicalCard);
                                return Filters.inSkirmishAgainst(CardType.MINION, Filters.lessStrengthThan(resistance)).accepts(game, physicalCard);
                            }
                        }));
        return action;
    }
}
