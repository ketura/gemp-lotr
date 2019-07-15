package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ReplaceInSkirmishEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Title: Battle Fever
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Event - Skirmish
 * Card Number: 1C5
 * Game Text: Exert Gimli to have him replace an unbound companion in a skirmish. He is strength +2 and damage +1 while in that skirmish.
 */
public class Card40_005 extends AbstractEvent {
    public Card40_005(Side side, int twilightCost, Culture culture, String name, Phase playableInPhase, Phase... additionalPlayableInPhases) {
        super(Side.FREE_PEOPLE, 2, Culture.DWARVEN, "Battle Fever", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Filters.name("Gimli"), Filters.notAssignedToSkirmish);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.name("Gimli")) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        action.appendEffect(
                                new ReplaceInSkirmishEffect(character, Filters.unboundCompanion));
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, character, 2)));
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new KeywordModifier(self, character, Keyword.DAMAGE, 1)));
                    }
                });
        return action;
    }
}
