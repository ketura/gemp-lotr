package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make a [GONDOR] or [SHIRE] companion bearing a hand weapon strength +2 and damage +1.
 */
public class Card1_102 extends AbstractEvent {
    public Card1_102() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Dager Strike", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose GONDOR of SHIRE companion bearing a hand weapon", Filters.or(Filters.culture(Culture.GONDOR), Filters.culture(Culture.SHIRE)), Filters.type(CardType.COMPANION), Filters.hasAttached(Filters.keyword(Keyword.HAND_WEAPON))) {
                    @Override
                    protected void cardSelected(PhysicalCard companion) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(companion), 2), Phase.SKIRMISH));
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new KeywordModifier(self, Filters.sameCard(companion), Keyword.DAMAGE), Phase.SKIRMISH));
                    }
                }
        );
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
