package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * 0
 * Dagger Strike
 * Gondor	Event • Skirmish
 * Make a [Gondor] or [Shire] companion bearing a hand weapon strength +2 and damage +1.
 */
public class Card20_187 extends AbstractEvent {
    public Card20_187() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Dagger Strike", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a companion", CardType.COMPANION, Filters.or(Culture.GONDOR, Culture.SHIRE),
                        Filters.hasAttached(PossessionClass.HAND_WEAPON)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, 2)));
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new KeywordModifier(self, card, Keyword.DAMAGE, 1)));
                    }
                });
        return action;
    }
}
