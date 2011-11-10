package com.gempukku.lotro.cards.set6.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

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
        super(15, 10, 5, Culture.GANDALF, Race.ENT, null, "Ent Horde", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, 2, Race.ENT, CardType.COMPANION);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return -2 * Filters.countSpottable(gameState, modifiersQuerying, Filters.or(Race.ENT, Filters.and(Race.HOBBIT, Filters.unboundCompanion)));
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, self, new Condition() {
                    @Override
                    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                        return Filters.countSpottable(gameState, modifiersQuerying, CardType.MINION) >
                                Filters.countSpottable(gameState, modifiersQuerying, CardType.COMPANION);
                    }
                }, Keyword.DEFENDER, 1));
    }
}
