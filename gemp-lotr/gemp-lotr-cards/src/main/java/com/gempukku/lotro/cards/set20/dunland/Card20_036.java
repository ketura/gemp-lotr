package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.CantBeLiberatedModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * 3
 * •Take Back Your Lands!
 * Dunland	Condition • Support Area
 * To play spot a [Dunland] man.
 * Plays on a site you control.
 * While you can spot a [Dunland] man, the Free Peoples Player may not liberate this site.
 */
public class Card20_036 extends AbstractAttachable {
    public Card20_036() {
        super(Side.SHADOW, CardType.CONDITION, 3, Culture.DUNLAND, null, "Take Back Your Lands!", null, true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, additionalAttachmentFilter, twilightModifier)
                && PlayConditions.canSpot(game, Culture.DUNLAND, Race.MAN);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.siteControlled(playerId);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantBeLiberatedModifier(self, Side.FREE_PEOPLE, new SpotCondition(Culture.DUNLAND, Race.MAN), Filters.hasAttached(self));
    }
}
