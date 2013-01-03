package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Dunlending Firebrand
 * Dunland	Minion • Man
 * 7 1 3
 * When you play Dunlending Firebrand, you may spot 2 other [Dunland] men to take control of a site.
 * The Free Peoples player may discard a Free Peoples condition to prevent this.
 */
public class Card20_003 extends AbstractMinion {
    public Card20_003() {
        super(2, 7, 1, 3, Race.MAN, Culture.DUNLAND, "Dunlending Firebrand");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
            && PlayConditions.canSpot(game, 2, Filters.not(self), Culture.DUNLAND, Race.MAN)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new PreventableEffect(action, new TakeControlOfASiteEffect(self, playerId), game.getGameState().getCurrentPlayerId(),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                    return new ChooseAndDiscardCardsFromPlayEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, Side.FREE_PEOPLE, CardType.CONDITION);

                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
