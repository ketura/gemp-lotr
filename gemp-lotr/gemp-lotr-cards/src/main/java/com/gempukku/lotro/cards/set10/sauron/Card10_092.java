package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Besieger. Fierce. Shadow: Exert this minion and spot another [SAURON] minion to control a site. If you
 * cannot spot 3 Free Peoples cultures, the Free Peoples player may exert a companion to prevent this.
 */
public class Card10_092 extends AbstractMinion {
    public Card10_092() {
        super(3, 9, 2, 5, Race.ORC, Culture.SAURON, "Mordor Pillager");
        addKeyword(Keyword.BESIEGER);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canSpot(game, Filters.not(self), Culture.SAURON, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            int cultures = GameUtils.getSpottableCulturesCount(game.getGameState(), game.getModifiersQuerying(), Side.FREE_PEOPLE);
            if (cultures < 3)
                action.appendEffect(
                        new PreventableEffect(action, new TakeControlOfASiteEffect(self, playerId), game.getGameState().getCurrentPlayerId(),
                                new PreventableEffect.PreventionCost() {
                                    @Override
                                    public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                        return new ChooseAndExertCharactersEffect(subAction, playerId, 1, 1, CardType.COMPANION) {
                                            @Override
                                            public String getText(LotroGame game) {
                                                return "Exert a companion";
                                            }
                                        };
                                    }
                                }));
            else
                action.appendEffect(
                        new TakeControlOfASiteEffect(self, playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
