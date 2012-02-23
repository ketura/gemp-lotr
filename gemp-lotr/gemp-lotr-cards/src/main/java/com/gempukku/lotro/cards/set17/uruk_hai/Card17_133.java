package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.PlayersCantUseSpecialAbilitiesModifier;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.CantPlayCardsModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Fierce. While this minion is assigned to a skirmish, each hunter companion loses its special abilities
 * and skirmish events may not be played in those companions skirmishes.
 */
public class Card17_133 extends AbstractMinion {
    public Card17_133() {
        super(3, 9, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Trooper");
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantPlayCardsModifier(self,
                        new AndCondition(
                                new SpotCondition(self, Filters.assignedToSkirmish),
                                new SpotCondition(CardType.COMPANION, Keyword.HUNTER, Filters.inSkirmish)
                        ), CardType.EVENT, Keyword.SKIRMISH));
        modifiers.add(
                new PlayersCantUseSpecialAbilitiesModifier(self,
                        new SpotCondition(self, Filters.assignedToSkirmish), CardType.COMPANION, Keyword.HUNTER));
        return modifiers;
    }
}
