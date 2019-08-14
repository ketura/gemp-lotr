package com.gempukku.lotro.cards.set15.rohan;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Valiant. Hunter 1. (While skirmishing a non-hunter character, this character is strength +1.)
 * To play, spot a [ROHAN] Man. While you can spot 3 hunters, Gamling is strength +3.
 */
public class Card15_126 extends AbstractCompanion {
    public Card15_126() {
        super(2, 6, 3, 6, Culture.ROHAN, Race.MAN, null, "Gamling", "The Old", true);
        addKeyword(Keyword.VALIANT);
        addKeyword(Keyword.HUNTER, 1);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, new SpotCondition(3, Keyword.HUNTER), 3));
}
}
