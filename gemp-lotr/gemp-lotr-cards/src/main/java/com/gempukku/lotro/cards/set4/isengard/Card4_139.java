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
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
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
 * Game Text: To play, exert Saruman or an [ISENGARD] Man. Plays on a Free Peoples Man. Each time another ally
 * or companion loses a skirmish, bearer must exert.
 */
public class Card4_139 extends AbstractAttachable {
    public Card4_139() {
        super(Side.SHADOW, CardType.CONDITION, 0, Culture.ISENGARD, null, "Banished");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.or(Filters.saruman, Filters.and(Culture.ISENGARD, Race.MAN)));
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, 1, Filters.or(Filters.saruman, Filters.and(Culture.ISENGARD, Race.MAN))));
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Side.FREE_PEOPLE, Race.MAN);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.losesSkirmish(game, effectResult,
                Filters.and(
                        Filters.not(Filters.hasAttached(self)),
                        Filters.or(CardType.ALLY, CardType.COMPANION)))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(action, self, Filters.hasAttached(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
