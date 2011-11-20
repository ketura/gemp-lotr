package com.gempukku.lotro.cards.set8.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 3
 * Resistance: 6
 * Game Text: Archer. Knight. To play, spot 3 [GONDOR] companions. While you can spot a fortification, Garrison of
 * Gondor is defender +1.
 */
public class Card8_036 extends AbstractCompanion {
    public Card8_036() {
        super(3, 8, 3, 6, Culture.GONDOR, Race.MAN, null, "Garrison of Gondor", true);
        addKeyword(Keyword.ARCHER);
        addKeyword(Keyword.KNIGHT);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, 3, Culture.GONDOR, CardType.COMPANION);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, self, new SpotCondition(Keyword.FORTIFICATION), Keyword.DEFENDER, 1));
    }
}
