package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: To play, spot Saruman or an [ISENGARD] Man. Plays on a Free Peoples Man. Each time the Free Peoples
 * player assigns bearer to a skirmish, exert each ally.
 */
public class Card4_172 extends AbstractAttachable {
    public Card4_172() {
        super(Side.SHADOW, CardType.CONDITION, 0, Culture.ISENGARD, null, "Rohan Is Mine");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.or(Filters.saruman, Filters.and(Culture.ISENGARD, Race.MAN)));
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Side.FREE_PEOPLE, Race.MAN);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.assignedToSkirmish(game, effectResult, Side.FREE_PEOPLE, Filters.hasAttached(self))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(action, self, CardType.ALLY));
            return Collections.singletonList(action);
        }
        return null;
    }
}
