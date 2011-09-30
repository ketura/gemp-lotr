package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.cards.modifiers.CancelStrengthBonusModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Artifact
 * Game Text: Bearer must be Frodo. The minion archery total is -1. Each minion skirmishing Frodo does not gain
 * strength bonuses from weapons and loses all damage bonuses.
 */
public class Card2_105 extends AbstractAttachableFPPossession {
    public Card2_105() {
        super(2, 0, 0, Culture.SHIRE, CardType.ARTIFACT, Keyword.ARMOR, "Mithril-coat", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Frodo");
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new ArcheryTotalModifier(self, Side.SHADOW, -1));
        modifiers.add(
                new CancelStrengthBonusModifier(self, Filters.and(Filters.and(Filters.keyword(Keyword.HAND_WEAPON), Filters.keyword(Keyword.RANGED_WEAPON)), Filters.attachedTo(Filters.inSkirmishAgainst(Filters.hasAttached(self))))));
        modifiers.add(
                new RemoveKeywordModifier(self, Filters.inSkirmishAgainst(Filters.hasAttached(self)), Keyword.DAMAGE));
        return modifiers;
    }
}
