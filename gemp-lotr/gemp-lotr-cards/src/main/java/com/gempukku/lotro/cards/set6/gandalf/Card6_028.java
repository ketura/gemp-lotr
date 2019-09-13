package com.gempukku.lotro.cards.set6.gandalf;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 15
 * Type: Companion • Ent
 * Strength: 10
 * Vitality: 5
 * Resistance: 6
 * Game Text: To play, spot 2 Ent companions. Ent Horde's twilight cost is -2 for each Ent or unbound Hobbit
 * you can spot. While you can spot more minions than companions, Ent Horde is defender +1.
 */
public class Card6_028 extends AbstractCompanion {
    public Card6_028() {
        super(15, 10, 5, 6, Culture.GANDALF, Race.ENT, null, "Ent Horde", null, true);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Race.ENT, CardType.COMPANION);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self, PhysicalCard target) {
        return -2 * (
                Filters.countActive(game, Filters.or(Race.ENT, Filters.and(Race.HOBBIT, Filters.unboundCompanion)))
                        + game.getModifiersQuerying().getSpotBonus(game, Race.ENT));
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, self, new Condition() {
                    @Override
                    public boolean isFullfilled(LotroGame game) {
                        return Filters.countActive(game, CardType.MINION) >
                                Filters.countActive(game, CardType.COMPANION);
                    }
                }, Keyword.DEFENDER, 1));
    }
}
