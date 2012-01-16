package com.gempukku.lotro.cards.set14.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Expanded Middle-earth
 * Side: Shadow
 * Culture: Man
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: At the start of the regroup phase, if you can spot more Man characters than any other race, make the move
 * limit -1 this turn.
 */
public class Card14_011 extends AbstractMinion {
    public Card14_011() {
        super(3, 8, 2, 4, Race.MAN, Culture.MEN, "Swarming Hillman");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)) {
            if (!hasMoreOrEqualCharactersInAnyRaceThanMen(game)) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new AddUntilEndOfTurnModifierEffect(
                                new MoveLimitModifier(self, -1)));
                return Collections.singletonList(action);
            }
        }
        return null;
    }

    private boolean hasMoreOrEqualCharactersInAnyRaceThanMen(LotroGame game) {
        int menCount = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.character, Race.MAN);
        for (Race race : Race.values()) {
            if (race != Race.MAN) {
                if (Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.character, race) >= menCount)
                    return true;
            }
        }

        return false;
    }
}
