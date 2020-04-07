package com.gempukku.lotro.cards.set18.uruk_hai;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Uruk-Hai
 * Twilight Cost: 0
 * Type: Minion • Uruk-Hai
 * Strength: 7
 * Vitality: 1
 * Site: 5
 * Game Text: Damage +1. To play, spot an [URUK-HAI] minion and you must control a site.
 */
public class Card18_129 extends AbstractMinion {
    public Card18_129() {
        super(0, 7, 1, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Sieger");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.URUK_HAI, CardType.MINION)
                && PlayConditions.canSpot(game, Filters.siteControlled(self.getOwner()));
    }
}
