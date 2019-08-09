package com.gempukku.lotro.cards.set31.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
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
        super(2, 0, 1, Culture.DWARVEN, CardType.ARTIFACT, PossessionClass.ARMOR, "Mithril-coat", null, true);
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Bilbo");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new ArcheryTotalModifier(self, Side.SHADOW, -1));
        modifiers.add(
                new TwilightCostModifier(self, Filters.and(Side.SHADOW, CardType.EVENT),
                        new Condition() {
            @Override
            public boolean isFullfilled(LotroGame game) {
                return game.getGameState().getSkirmish() != null
				&& game.getGameState().getSkirmish().getFellowshipCharacter() == self.getAttachedTo();
			}
		}, 2));
        return modifiers;
    }
}
