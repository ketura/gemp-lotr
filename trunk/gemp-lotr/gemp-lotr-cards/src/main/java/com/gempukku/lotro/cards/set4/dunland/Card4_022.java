package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 6
 * Type: Minion • Man
 * Strength: 14
 * Vitality: 2
 * Site: 3
 * Game Text: While you control 2 sites, skip the archery phase. While you control 3 sites, each of your [DUNLAND] Men
 * is fierce. While you control 4 sites, each of your [DUNLAND] Men is damage +1.
 */
public class Card4_022 extends AbstractMinion {
    public Card4_022() {
        super(6, 14, 2, 3, Race.MAN, Culture.DUNLAND, "Hillman Horde", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new AbstractModifier(self, "Skip the archery phase", null, new ModifierEffect[]{ModifierEffect.ACTION_MODIFIER}) {
                    @Override
                    public boolean shouldSkipPhase(GameState gameState, ModifiersQuerying modifiersQuerying, Phase phase, String playerId) {
                        return phase == Phase.ARCHERY
                                && Filters.countSpottable(gameState, modifiersQuerying, Filters.siteControlled(self.getOwner())) >= 2;
                    }
                });
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.owner(self.getOwner()), Filters.culture(Culture.DUNLAND), Filters.race(Race.MAN)),
                        new SpotCondition(3, Filters.siteControlled(self.getOwner())), Keyword.FIERCE, 1));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.owner(self.getOwner()), Filters.culture(Culture.DUNLAND), Filters.race(Race.MAN)),
                        new SpotCondition(4, Filters.siteControlled(self.getOwner())), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
