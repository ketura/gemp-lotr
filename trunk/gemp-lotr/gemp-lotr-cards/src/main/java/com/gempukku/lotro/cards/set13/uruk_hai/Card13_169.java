package com.gempukku.lotro.cards.set13.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
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
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 12
 * Vitality: 2
 * Site: 5
 * Game Text: To play, spot an [URUK-HAI] minion. While the fellowship is in region 1, each Uruk-hai is damage +1. While
 * the fellowship is in region 2, each companion is strength -1. While the fellowship is in region 3, each
 * unbound companion is resistance -2.
 */
public class Card13_169 extends AbstractMinion {
    public Card13_169() {
        super(4, 12, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk Blitz", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.URUK_HAI, CardType.MINION);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Race.URUK_HAI, new LocationCondition(Filters.region(1)), Keyword.DAMAGE, 1));
        modifiers.add(
                new StrengthModifier(self, CardType.COMPANION, new LocationCondition(Filters.region(2)), -1));
        modifiers.add(
                new ResistanceModifier(self, Filters.unboundCompanion, new LocationCondition(Filters.region(3)), -2));
        return modifiers;
    }
}
