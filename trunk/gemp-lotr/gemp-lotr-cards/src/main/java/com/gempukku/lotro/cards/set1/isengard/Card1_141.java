package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, spot Saruman or an Uruk-hai. Plays to your support area. Each archer companion and archer ally
 * is strength -1.
 */
public class Card1_141 extends AbstractPermanent {
    public Card1_141() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "Their Arrows Enrage");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.race(Race.URUK_HAI), Filters.saruman));
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Filters.keyword(Keyword.ARCHER), Filters.or(CardType.COMPANION, CardType.ALLY)), -1);
    }
}
