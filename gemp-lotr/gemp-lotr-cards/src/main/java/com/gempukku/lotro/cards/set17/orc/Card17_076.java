package com.gempukku.lotro.cards.set17.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.CantPlaySiteByFPPlayerModifier;
import com.gempukku.lotro.cards.modifiers.CantReplaceSiteByFPPlayerModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
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
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 8
 * Type: Minion • Orc
 * Strength: 18
 * Vitality: 4
 * Site: 4
 * Game Text: To play, spot an [ORC] Orc. While at an underground site, this minion is fierce and the Free Peoples
 * player may not replace sites or play a site from his or her adventure deck.
 */
public class Card17_076 extends AbstractMinion {
    public Card17_076() {
        super(8, 18, 4, 4, Race.ORC, Culture.ORC, "Orkish Fiend");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ORC, Race.ORC);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, self, new LocationCondition(Keyword.UNDERGROUND), Keyword.FIERCE, 1));
        modifiers.add(
                new CantReplaceSiteByFPPlayerModifier(self, new LocationCondition(Keyword.UNDERGROUND), Filters.any));
        modifiers.add(
                new CantPlaySiteByFPPlayerModifier(self, new LocationCondition(Keyword.UNDERGROUND)));
        return modifiers;
    }
}
