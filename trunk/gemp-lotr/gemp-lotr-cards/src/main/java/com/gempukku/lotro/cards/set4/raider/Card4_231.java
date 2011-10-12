package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

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
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.RAIDER, Zone.SUPPORT, "Eastern Emyn Muil", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_MOVE_FROM
                && game.getGameState().getCurrentSiteNumber() == 2 && game.getGameState().getCurrentSiteBlock() == Block.TWO_TOWERS) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ExertCharactersEffect(self, Filters.and(Filters.type(CardType.COMPANION), Filters.keyword(Keyword.RING_BOUND))));
            action.appendEffect(
                    new ExertCharactersEffect(self, Filters.and(Filters.type(CardType.COMPANION), Filters.keyword(Keyword.RING_BOUND))));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.MANEUVER, self, 0)
                && PlayConditions.canExert(self, game, Filters.culture(Culture.RAIDER), Filters.race(Race.MAN))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.RAIDER), Filters.race(Race.MAN)));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.type(CardType.COMPANION), Filters.keyword(Keyword.RING_BOUND)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
