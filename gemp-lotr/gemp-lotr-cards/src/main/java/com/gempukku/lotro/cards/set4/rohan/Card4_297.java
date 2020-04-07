package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make a [ROHAN] Man strength +2 (and damage +2 if mounted).
 */
public class Card4_297 extends AbstractEvent {
    public Card4_297() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Work for the Sword", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose character", Culture.ROHAN, Race.MAN) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), 2)));

                        boolean mounted = Filters.mounted.accepts(game, card);
                        if (mounted)
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new KeywordModifier(self, Filters.sameCard(card), Keyword.DAMAGE, 2)));
                    }
                });
        return action;
    }
}
