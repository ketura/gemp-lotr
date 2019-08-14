package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.CantReplaceSiteByFPPlayerModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 0
 * Type: Condition • Site
 * Game Text: To play, exert an [ORC] minion. Plays on the fellowship’s current site. This site cannot be replaced.
 * While the fellowship is at this site, each Troll is strength +1.
 */
public class Card15_115 extends AbstractAttachable {
    public Card15_115() {
        super(Side.SHADOW, CardType.CONDITION, 0, Culture.ORC, null, "Pummeling Blow", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.currentSite;
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.ORC, CardType.MINION);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, 1, Culture.ORC, CardType.MINION));
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantReplaceSiteByFPPlayerModifier(self, null, Filters.hasAttached(self)));
        modifiers.add(
                new StrengthModifier(self, Race.TROLL, new LocationCondition(Filters.hasAttached(self)), 1));
        return modifiers;
    }
}
