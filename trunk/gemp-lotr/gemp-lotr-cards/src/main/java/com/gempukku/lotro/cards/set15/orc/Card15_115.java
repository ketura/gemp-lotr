package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.CantReplaceSiteByFPPlayerModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

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
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.currentSite;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, additionalAttachmentFilter, twilightModifier)
                && PlayConditions.canExert(self, game, Culture.ORC, CardType.MINION);
    }

    @Override
    public AttachPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, Filterable additionalAttachmentFilter, int twilightModifier) {
        final AttachPermanentAction playCardAction = super.getPlayCardAction(playerId, game, self, additionalAttachmentFilter, twilightModifier);
        playCardAction.appendCost(
                new ChooseAndExertCharactersEffect(playCardAction, playerId, 1, 1, Culture.ORC, CardType.MINION));
        return playCardAction;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantReplaceSiteByFPPlayerModifier(self, null, Filters.hasAttached(self)));
        modifiers.add(
                new StrengthModifier(self, Race.TROLL, new LocationCondition(Filters.hasAttached(self)), 1));
        return modifiers;
    }
}
