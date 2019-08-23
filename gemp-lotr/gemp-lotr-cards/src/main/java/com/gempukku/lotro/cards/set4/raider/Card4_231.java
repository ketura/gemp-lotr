package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. Each Ring-bound companion moving from site 2T must exert twice.
 * Maneuver: Discard this condition and exert a [RAIDER] Man to exert a Ringbound companion.
 */
public class Card4_231 extends AbstractPermanent {
    public Card4_231() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.RAIDER, "Eastern Emyn Muil", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesFrom(game, effectResult, Filters.siteBlock(SitesBlock.TWO_TOWERS), Filters.siteNumber(2))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(action, self, Filters.and(CardType.COMPANION, Keyword.RING_BOUND)));
            action.appendEffect(
                    new ExertCharactersEffect(action, self, Filters.and(CardType.COMPANION, Keyword.RING_BOUND)));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canExert(self, game, Culture.RAIDER, Race.MAN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.RAIDER, Race.MAN));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Keyword.RING_BOUND));
            return Collections.singletonList(action);
        }
        return null;
    }
}
