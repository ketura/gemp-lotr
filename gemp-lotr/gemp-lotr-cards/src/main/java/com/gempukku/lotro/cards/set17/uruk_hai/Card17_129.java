package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.RemoveThreatExtraPlayCostModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 6
 * Type: Minion • Uruk-Hai
 * Strength: 17
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. To play, remove 2 threats.
 */
public class Card17_129 extends AbstractMinion {
    public Card17_129() {
        super(6, 17, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Legion");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new RemoveThreatExtraPlayCostModifier(self, 2, null, self));
    }
}
