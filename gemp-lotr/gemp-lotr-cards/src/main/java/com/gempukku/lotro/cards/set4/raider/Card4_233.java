package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
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
        super(Side.SHADOW, 2, Culture.RAIDER, "Fearless", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new ChooseActiveCardEffect(self, playerId, "Choose Southron", Keyword.SOUTHRON) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Make a Southron strength +1 for each burden you spot (limit +5)";
                    }

                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int bonus = Math.min(5, game.getGameState().getBurdens());
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, bonus)));
                    }
                });
        possibleEffects.add(
                new ChooseActiveCardEffect(self, playerId, "Choose RAIDER Man", Culture.RAIDER, Race.MAN) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Make a RAIDER Man strength +2";
                    }

                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, card, 2)));
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
