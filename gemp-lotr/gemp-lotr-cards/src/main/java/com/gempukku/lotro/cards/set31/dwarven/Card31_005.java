package com.gempukku.lotro.cards.set31.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.cards.modifiers.CancelStrengthBonusTargetModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Artifact
 * Vitality: +1
 * Game Text: Bearer must be Bilbo. The minion archery total is -1. While Bilbo is skirmishing, the twilight cost
 * of each Shadow event is +2.
 */
public class Card31_005 extends AbstractAttachableFPPossession {
    public Card31_005() {
        super(2, 0, 1, Culture.SHIRE, CardType.ARTIFACT, PossessionClass.ARMOR, "Mithril-coat", null, true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Bilbo");
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new ArcheryTotalModifier(self, Side.SHADOW, -1));
        modifiers.add(
                new TwilightCostModifier(self, Filters.and(Side.SHADOW, CardType.POSSESSION),
                        new Condition() {
            @Override
            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                return gameState.getSkirmish() != null
				&& gameState.getSkirmish().getFellowshipCharacter() == self.getAttachedTo();
			}
		}, 2));
        return modifiers;
    }
}