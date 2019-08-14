package com.gempukku.lotro.cards.set2.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

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
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.MAN;
    }

    @Override
    public boolean isExtraPossessionClass(LotroGame game, PhysicalCard self, PhysicalCard attachedTo) {
        return true;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        Condition condition = new Condition() {
            @Override
            public boolean isFullfilled(LotroGame game) {
                return game.getGameState().getSkirmish() != null
                        && game.getGameState().getSkirmish().getFellowshipCharacter() == self.getAttachedTo()
                        && Filters.filter(game.getGameState().getSkirmish().getShadowCharacters(), game, Race.NAZGUL).size() > 0;
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
