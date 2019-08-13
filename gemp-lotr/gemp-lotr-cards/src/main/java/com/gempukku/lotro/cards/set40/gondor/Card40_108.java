package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.modifiers.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Title: *Flaming Brand
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Possession - Hand Weapon
 * Strength: +1
 * Card Number: 1R108
 * Game Text: Bearer must be a [GONDOR] Man. This weapon may be borne in addition to one other hand weapon. Bearer is strength +2 and damage +1 while skirmishing a Nazgul.
 */
public class Card40_108 extends AbstractAttachableFPPossession {
    public Card40_108() {
        super(0, 1, 0, Culture.GONDOR, PossessionClass.HAND_WEAPON, "Flaming Brand", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.MAN;
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
        modifiers.add(
                new ExtraPossessionClassModifier(self, self));
        return modifiers;
    }
}
