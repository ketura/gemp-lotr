package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromStackedOnTopOfDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndStackCardsFromDiscardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •The Mirror of Galadriel, Revealer of Portents
 * Elven	Artifact • Support Area
 * At the beginning of the fellowship phase you may spot Galadriel to place a card stacked here on top of your draw deck.
 * Regroup: Exert Galadriel to stack an [Elven] card from your discard pile here.
 */
public class Card20_096 extends AbstractPermanent {
    public Card20_096() {
        super(Side.FREE_PEOPLE, 2, CardType.ARTIFACT, Culture.ELVEN, "The Mirror of Galadriel", "Revealer of Portents", true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.FELLOWSHIP)
                && PlayConditions.canSpot(game, Filters.galadriel)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPutCardFromStackedOnTopOfDeckEffect(action, playerId, 1, 1, self, Filters.any));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && PlayConditions.canExert(self, game, Filters.galadriel)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.galadriel));
            action.appendEffect(
                    new ChooseAndStackCardsFromDiscardEffect(action, playerId, 1, 1, self, Culture.ELVEN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
