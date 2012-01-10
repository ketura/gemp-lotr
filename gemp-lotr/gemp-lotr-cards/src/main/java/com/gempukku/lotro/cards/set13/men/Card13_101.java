package com.gempukku.lotro.cards.set13.men;

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

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: To play, spot a [MEN] minion. While the fellowship is in region 1, each [MEN] minion cannot take wounds.
 * While the fellowship is in region 2, each [MEN] minion is an archer. While the fellowship is in region 3, each [MEN]
 * minion is fierce.
 */
public class Card13_101 extends AbstractMinion {
    public Card13_101() {
        super(4, 11, 3, 4, Race.MAN, Culture.MEN, "Voice of the Desert", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.MEN, CardType.MINION);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantTakeWoundsModifier(self, new LocationCondition(Filters.region(1)), Filters.and(Culture.MEN, CardType.MINION)));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Culture.MEN, CardType.MINION), new LocationCondition(Filters.region(2)), Keyword.ARCHER, 1));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Culture.MEN, CardType.MINION), new LocationCondition(Filters.region(3)), Keyword.FIERCE, 1));
        return modifiers;
    }
}
