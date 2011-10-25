package com.gempukku.lotro.cards.set2.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be a Man. This weapon may be borne in addition to 1 other hand weapon. Bearer is strength +2
 * and damage +1 while skirmishing a Nazgul.
 */
public class Card2_032 extends AbstractAttachableFPPossession {
    public Card2_032() {
        super(0, 1, 0, Culture.GONDOR, PossessionClass.HAND_WEAPON, "Flaming Brand");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.race(Race.MAN);
    }

    @Override
    public boolean isExtraPossessionClass() {
        return true;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(final PhysicalCard self) {
        Condition condition = new Condition() {
            @Override
            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                return gameState.getSkirmish() != null
                        && gameState.getSkirmish().getFellowshipCharacter() == self.getAttachedTo()
                        && Filters.filter(gameState.getSkirmish().getShadowCharacters(), gameState, modifiersQuerying, Filters.race(Race.NAZGUL)).size() > 0;
            }
        };

        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), condition, 2));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), condition, Keyword.DAMAGE, 1));
        return modifiers;
    }
}
