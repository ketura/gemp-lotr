package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.modifiers.SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays on a site you control. While an Uruk-hai is stacked on this site, the Free Peoples player may
 * not play skirmish events or use skirmish special abilities.
 */
public class Card4_167 extends AbstractAttachable {
    public Card4_167() {
        super(Side.SHADOW, CardType.CONDITION, 0, Culture.ISENGARD, null, "Pillage of Rohan");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.siteControlled(playerId);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new SidePlayerCantPlayPhaseEventsOrSpecialAbilitiesModifier(self,
                        new SpotCondition(self, Filters.hasStacked(Race.URUK_HAI)), Side.FREE_PEOPLE, Phase.SKIRMISH));
    }
}
