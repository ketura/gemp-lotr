package com.gempukku.lotro.cards.set13.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: To play, spot an [ORC] minion. While the fellowship is in region 1, [ORC] Orcs cannot take wounds. While
 * the fellowship is in region 2, the current site gains underground. While the fellowship is in region 3, each [ORC]
 * Orc is strength +2.
 */
public class Card13_104 extends AbstractMinion {
    public Card13_104() {
        super(4, 11, 3, 4, Race.ORC, Culture.ORC, "Chamber Patrol", null, true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ORC, CardType.MINION);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantTakeWoundsModifier(self, new LocationCondition(Filters.region(1)), Filters.and(Culture.ORC, Race.ORC)));
        modifiers.add(
                new KeywordModifier(self, Filters.currentSite, new LocationCondition(Filters.region(2)), Keyword.UNDERGROUND, 1));
        modifiers.add(
                new StrengthModifier(self, Filters.and(Culture.ORC, Race.ORC), new LocationCondition(Filters.region(3)), 2));
        return modifiers;
    }
}
