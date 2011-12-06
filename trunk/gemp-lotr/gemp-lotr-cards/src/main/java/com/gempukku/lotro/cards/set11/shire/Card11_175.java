package com.gempukku.lotro.cards.set11.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Spot X Hobbits to make an unbound Hobbit resistance +X.
 */
public class Card11_175 extends AbstractEvent {
    public Card11_175() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "A Task Now to Be Done", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ForEachYouSpotEffect(playerId, Race.HOBBIT) {
                    @Override
                    protected void spottedCards(final int spotCount) {
                        action.appendEffect(
                                new ChooseActiveCardEffect(self, playerId, "Choose an unbound Hobbit", Filters.unboundCompanion, Race.HOBBIT) {
                                    @Override
                                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                                        action.appendEffect(
                                                new AddUntilEndOfPhaseModifierEffect(
                                                        new ResistanceModifier(self, card, spotCount), Phase.SKIRMISH));
                                    }
                                });
                    }
                });
        return action;
    }
}
