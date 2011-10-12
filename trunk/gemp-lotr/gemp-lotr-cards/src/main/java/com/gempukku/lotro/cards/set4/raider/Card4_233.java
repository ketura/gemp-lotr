package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Skirmish: Make a Southron strength +1 for each burden you spot (limit +5), or make a [RAIDER] Man
 * strength +2.
 */
public class Card4_233 extends AbstractEvent {
    public Card4_233() {
        super(Side.SHADOW, Culture.RAIDER, "Fearless", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseActiveCardEffect(self, playerId, "Choose Southron", Filters.keyword(Keyword.SOUTHRON)) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Make a Southron strength +1 for each burden you spot (limit +5)";
                    }

                    @Override
                    protected void cardSelected(PhysicalCard card) {
                        int bonus = Math.min(5, game.getGameState().getBurdens());
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(self), bonus), Phase.SKIRMISH));
                    }
                });
        possibleEffects.add(
                new ChooseActiveCardEffect(self, playerId, "Choose RAIDER Man", Filters.culture(Culture.RAIDER), Filters.race(Race.MAN)) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Make a RAIDER Man strength +2";
                    }

                    @Override
                    protected void cardSelected(PhysicalCard card) {
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(self), 2), Phase.SKIRMISH));
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }
}
