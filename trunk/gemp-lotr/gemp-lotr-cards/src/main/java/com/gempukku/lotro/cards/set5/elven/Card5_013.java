package com.gempukku.lotro.cards.set5.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.DoesNotAddToArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: To play, spot an Elf. While Taurnil bears a ranged weapon, the twilight cost of each Shadow possession
 * is +2, and he does not add to the fellowship archery total.
 */
public class Card5_013 extends AbstractCompanion {
    public Card5_013() {
        super(2, 6, 3, Culture.ELVEN, Race.ELF, null, "Taurnil", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new TwilightCostModifier(self, Filters.and(Side.SHADOW, CardType.POSSESSION),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return Filters.countActive(gameState, modifiersQuerying, Filters.sameCard(self), Filters.hasAttached(Filters.possessionClass(PossessionClass.RANGED_WEAPON))) > 0;
                            }
                        }, 2));
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
}
