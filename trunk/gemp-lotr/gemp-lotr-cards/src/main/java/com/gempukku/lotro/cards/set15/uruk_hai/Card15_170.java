package com.gempukku.lotro.cards.set15.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 10
 * Vitality: 3
 * Site: 5
 * Game Text: To play, spot an [URUK-HAI] minion. While you can spot a fierce minion, this minion is fierce.
 * While you can spot a hunter, this minion gains hunter 1. While you can spot a character that is damage +1,
 * this minion is damage +1.
 */
public class Card15_170 extends AbstractMinion {
    public Card15_170() {
        super(4, 10, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Sentry Uruk", null, true);
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
                new KeywordModifier(self, self, new SpotCondition(CardType.MINION, Keyword.FIERCE), Keyword.FIERCE, 1));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(Keyword.HUNTER), Keyword.HUNTER, 1));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(Filters.character, Keyword.DAMAGE), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
