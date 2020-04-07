package com.gempukku.lotro.cards.set8.gondor;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.cost.AddThreatExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.SpotExtraPlayCostModifier;

import java.util.Arrays;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 5
 * Type: Companion • Wraith
 * Strength: 9
 * Vitality: 3
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Enduring. To play, spot 2 exhausted [GONDOR] Wraiths and add 2 threats. While Shadow Host is exhausted,
 * it is defender +1.
 */
public class Card8_043 extends AbstractCompanion {
    public Card8_043() {
        super(5, 9, 3, 6, Culture.GONDOR, Race.WRAITH, Signet.ARAGORN, "Shadow Host", null, true);
        addKeyword(Keyword.ENDURING);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new SpotExtraPlayCostModifier(self, self, null, 2, Culture.GONDOR, Race.WRAITH, Filters.exhausted),
                new AddThreatExtraPlayCostModifier(self, 2, null, self));
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new KeywordModifier(self, Filters.and(self, Filters.exhausted), Keyword.DEFENDER, 1));
    }
}
