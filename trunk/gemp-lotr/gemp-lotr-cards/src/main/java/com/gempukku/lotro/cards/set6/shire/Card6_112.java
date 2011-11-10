package com.gempukku.lotro.cards.set6.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
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
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Exert an unbound Hobbit to make him strength +1 and damage +1 for each [GANDALF] companion
 * you can spot.
 */
public class Card6_112 extends AbstractEvent {
    public Card6_112() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "Long Slow Wrath", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, Race.HOBBIT, Filters.unboundCompanion);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose unbound Hobbit", Race.HOBBIT, Filters.unboundCompanion) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int count = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Culture.GANDALF, CardType.COMPANION);
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, count), Phase.SKIRMISH));
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new KeywordModifier(self, card, Keyword.DAMAGE, count), Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
