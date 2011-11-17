package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.modifiers.CantHealModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, spot a [SAURON] Orc. Plays on a companion (except an Elf). While at a sanctuary, bearer cannot
 * heal.
 */
public class Card1_283 extends AbstractAttachable {
    public Card1_283() {
        super(Side.SHADOW, CardType.CONDITION, 1, Culture.SAURON, null, "You Bring Great Evil");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(CardType.COMPANION, Filters.not(Race.ELF));
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Culture.SAURON, Race.ORC);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantHealModifier(self, new LocationCondition(Keyword.SANCTUARY), Filters.hasAttached(self));
    }
}
