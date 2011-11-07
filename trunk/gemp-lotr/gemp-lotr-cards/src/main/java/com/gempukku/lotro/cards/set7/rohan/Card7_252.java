package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: To play, spot a [ROHAN] Man. Plays on a site. This site is a plains. Each [ROHAN] Man is strength +1
 * at this site.
 */
public class Card7_252 extends AbstractAttachable {
    public Card7_252() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.ROHAN, null, "Strong Arms");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return CardType.SITE;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.PLAINS));
        modifiers.add(
                new StrengthModifier(self, Filters.and(Culture.ROHAN, Race.MAN), new LocationCondition(Filters.hasAttached(self)), 1));
        return modifiers;
    }
}
