package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make Smeagol strength +1 for each [GOLLUM] card you spot.
 */
public class Card7_064 extends AbstractEvent {
    public Card7_064() {
        super(Side.FREE_PEOPLE, 1, Culture.GOLLUM, "Nasty", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ForEachYouSpotEffect(playerId, Culture.GOLLUM) {
                    @Override
                    protected void spottedCards(int spotCount) {
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.smeagol, spotCount), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
