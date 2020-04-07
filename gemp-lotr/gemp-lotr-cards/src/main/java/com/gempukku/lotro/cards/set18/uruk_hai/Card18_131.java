package com.gempukku.lotro.cards.set18.uruk_hai;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Uruk-Hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 10
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. To play, spot an [URUK-HAI] minion.
 */
public class Card18_131 extends AbstractMinion {
    public Card18_131() {
        super(3, 10, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Uruk");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.URUK_HAI, CardType.MINION);
    }
}
