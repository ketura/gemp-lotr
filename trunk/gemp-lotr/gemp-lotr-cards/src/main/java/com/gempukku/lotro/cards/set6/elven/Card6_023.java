package com.gempukku.lotro.cards.set6.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.cards.modifiers.DoesNotAddToArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 4
 * Type: Companion • Elf
 * Strength: 9
 * Vitality: 3
 * Resistance: 6
 * Game Text: To play, spot 3 Elf companions. While Naith Warband bears a ranged weapon, it takes no more than 1 wound
 * during each skirmish phase and does not add to the fellowship archery total.
 */
public class Card6_023 extends AbstractCompanion {
    public Card6_023() {
        super(4, 9, 3, Culture.ELVEN, Race.ELF, null, "Naith Warband", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canSpot(game, 3, Race.ELF, CardType.COMPANION);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new DoesNotAddToArcheryTotalModifier(self, Filters.sameCard(self),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return Filters.countActive(gameState, modifiersQuerying, Filters.sameCard(self), Filters.hasAttached(Filters.possessionClass(PossessionClass.RANGED_WEAPON))) > 0;
                            }
                        }));
        return modifiers;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.isWounded(effectResult, self)
                && game.getGameState().getCurrentPhase() == Phase.SKIRMISH) {
            if (Filters.hasAttached(PossessionClass.RANGED_WEAPON).accepts(game.getGameState(), game.getModifiersQuerying(), self)) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new AddUntilEndOfPhaseModifierEffect(
                                new CantTakeWoundsModifier(self, Filters.and(self, Filters.hasAttached(PossessionClass.RANGED_WEAPON))), Phase.SKIRMISH));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
