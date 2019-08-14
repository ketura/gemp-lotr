package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 2
 * Glimpse of Twilight
 * Ringwraith	Condition • Support Area
 * Twilight. To play, exert a Nazgul.
 * While the Ring-bearer wears The One Ring, Nazgul are twilight and fierce.
 * Regroup: Discard this condition (or a Nazgul) to exert the Ring-bearer.
 */
public class Card20_292 extends AbstractPermanent {
    public Card20_292() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.WRAITH, "Glimpse of Twilight");
        addKeyword(Keyword.TWILIGHT);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        Condition wearRingCondition = new Condition() {
            @Override
            public boolean isFullfilled(LotroGame game) {
                return game.getGameState().isWearingRing();
            }
        };
        modifiers.add(
                new KeywordModifier(self, Race.NAZGUL, wearRingCondition, Keyword.FIERCE, 1));
        modifiers.add(
                new KeywordModifier(self, Race.NAZGUL, wearRingCondition, Keyword.TWILIGHT, 1));
        modifiers.add(
                new ExertExtraPlayCostModifier(self, self, null, Race.NAZGUL));
        return modifiers;
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && (PlayConditions.canSelfDiscard(self, game)
                || PlayConditions.canDiscardFromPlay(self, game, Race.NAZGUL))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new SelfDiscardEffect(self));
            possibleCosts.add(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Race.NAZGUL) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard a Nazgul";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.ringBearer));
            return Collections.singletonList(action);
        }
        return null;
    }
}
