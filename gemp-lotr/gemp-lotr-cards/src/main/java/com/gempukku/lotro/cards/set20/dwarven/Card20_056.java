package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndStackCardsFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Gimli's Pipe
 * Dwarven	Possession • Pipe
 * Bearer must be Gimli.
 * Manuever: Discard a pipeweed possession and spot X pipes to stack X [Dwarven] events from your discard pile on
 * a [Dwarven] support area condition.
 */
public class Card20_056 extends AbstractAttachableFPPossession{
    public Card20_056() {
        super(1, 0, 0, Culture.DWARVEN, PossessionClass.PIPE,  "Gimli's Pipe", null, true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.gimli;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canDiscardFromPlay(self, game, PossessionClass.PIPEWEED, CardType.POSSESSION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendCost(
                    new ForEachYouSpotEffect(playerId, PossessionClass.PIPE) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            action.appendEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose a DWARVEN condition in your support area", Culture.DWARVEN, CardType.CONDITION, Zone.SUPPORT) {
                                        @Override
                                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                                            action.appendEffect(
                                                    new ChooseAndStackCardsFromDiscardEffect(action, playerId, 1, 1, card, Culture.DWARVEN, CardType.EVENT));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
