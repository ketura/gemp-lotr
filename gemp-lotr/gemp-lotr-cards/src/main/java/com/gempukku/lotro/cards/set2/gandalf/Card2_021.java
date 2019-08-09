package com.gempukku.lotro.cards.set2.gandalf;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.modifiers.ShadowPlayersCantLookAtYourHandModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Ally • Home 3 • Man
 * Strength: 5
 * Vitality: 2
 * Site: 3
 * Game Text: To play, spot Gandalf. Shadow players may not look at or reveal cards in your hand.
 */
public class Card2_021 extends AbstractAlly {
    public Card2_021() {
        super(2, SitesBlock.FELLOWSHIP, 3, 5, 2, Race.MAN, Culture.GANDALF, "Erland", "Advisor to Brand", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && Filters.canSpot(game, Filters.gandalf);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new ShadowPlayersCantLookAtYourHandModifier(self, self.getOwner()));
}
}
