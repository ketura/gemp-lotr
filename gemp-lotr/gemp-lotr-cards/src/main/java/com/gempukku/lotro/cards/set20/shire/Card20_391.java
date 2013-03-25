package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Herblore of the Shire
 * Possession • Support Area
 * To play spot an unbound Hobbit.
 * Skirmish: Discard a pipeweed possession to make a companion strength +1 for each pipe you can spot (limit +3).
 * http://lotrtcg.org/coreset/shire/herbloreoftheshire(r2).jpg
 */
public class Card20_391 extends AbstractPermanent {
    public Card20_391() {
        super(Side.FREE_PEOPLE, 2, CardType.POSSESSION, Culture.SHIRE, Zone.SUPPORT, "Herblore of the Shire", null, true);
        addKeyword(Keyword.TALE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Filters.unboundCompanion, Race.HOBBIT);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromPlay(self, game, CardType.POSSESSION, Keyword.PIPEWEED)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION, Keyword.PIPEWEED));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                            new CountSpottableEvaluator(3, PossessionClass.PIPE), CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
