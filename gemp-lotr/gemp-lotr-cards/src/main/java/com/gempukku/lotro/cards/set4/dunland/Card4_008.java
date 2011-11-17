package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Skirmish: Make a [DUNLAND] Man strength +1 for each companion you spot.
 */
public class Card4_008 extends AbstractOldEvent {
    public Card4_008() {
        super(Side.SHADOW, Culture.DUNLAND, "Death to the Strawheads", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Chooose DUNLAND Man", Culture.DUNLAND, Race.MAN) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int bonus = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION);
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), bonus), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
