package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Skirmish: Make a [DUNLAND] Man strength +1 for each companion you spot.
 */
public class Card4_008 extends AbstractEvent {
    public Card4_008() {
        super(Side.SHADOW, Culture.DUNLAND, "Death to the Strawheads", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Chooose DUNLAND Man", Filters.culture(Culture.DUNLAND), Filters.race(Race.MAN)) {
                    @Override
                    protected void cardSelected(PhysicalCard card) {
                        int bonus = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION));
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), bonus), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
