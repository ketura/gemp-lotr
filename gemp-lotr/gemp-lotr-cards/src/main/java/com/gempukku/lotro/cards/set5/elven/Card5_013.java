package com.gempukku.lotro.cards.set5.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.DoesNotAddToArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
        super(2, 6, 3, 6, Culture.ELVEN, Race.ELF, null, "Taurnil", "Sharp-eyed Bowman", true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.ELF);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new TwilightCostModifier(self, Filters.and(Side.SHADOW, CardType.POSSESSION),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                return Filters.countActive(game, self, Filters.hasAttached(PossessionClass.RANGED_WEAPON)) > 0;
                            }
                        }, 2));
        modifiers.add(
                new DoesNotAddToArcheryTotalModifier(self, self,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                return Filters.countActive(game, self, Filters.hasAttached(PossessionClass.RANGED_WEAPON)) > 0;
                            }
                        }));
        return modifiers;
    }
}
