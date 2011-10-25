package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ForEachTwilightTokenYouSpotEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make Gandalf strength +1 for each twilight token you spot (limit +5).
 */
public class Card4_105 extends AbstractOldEvent {
    public Card4_105() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Under the Living Earth", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ForEachTwilightTokenYouSpotEffect(playerId) {
                    @Override
                    protected void twilightTokensSpotted(int twilightTokens) {
                        int bonus = Math.min(5, twilightTokens);
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.name("Gandalf"), bonus), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
