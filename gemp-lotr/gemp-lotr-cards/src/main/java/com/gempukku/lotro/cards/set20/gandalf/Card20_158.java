package com.gempukku.lotro.cards.set20.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CheckTurnLimitEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Gandalf's Pipe
 * Gandalf	Possession • Pipe
 * Bearer must be Gandalf.
 * At the start of the Fellowship phase, you may discard a pipeweed possession and spot 2 other pipes to discard up to 2 Shadow conditions.
 */
public class Card20_158 extends AbstractAttachableFPPossession {
    public Card20_158() {
        super(1, 0, 0, Culture.GANDALF, PossessionClass.PIPE, "Gandalf's Pipe", null, true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.gandalf;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canDiscardFromPlay(self, game, CardType.POSSESSION, Keyword.PIPEWEED)
                && PlayConditions.canSpot(game, 2, PossessionClass.PIPE)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION, Keyword.PIPEWEED));
            action.appendEffect(
                    new CheckTurnLimitEffect(action, self, 1,
                            new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 0, 2, Side.SHADOW, CardType.CONDITION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
