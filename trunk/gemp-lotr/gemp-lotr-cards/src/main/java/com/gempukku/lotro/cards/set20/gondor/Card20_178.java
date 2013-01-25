package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PlayNextSiteEffect;
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
 * •Aragorn's Pipe
 * Gondor	Possession •  Pipe
 * Bearer must be Aragorn.
 * Fellowship or Regroup: Spot X pipes and discard a pipeweed possession to play the fellowship's next site,
 * where X is the site number of the next site.
 */
public class Card20_178 extends AbstractAttachableFPPossession {
    public Card20_178() {
        super(1, 0, 0, Culture.GONDOR, PossessionClass.PIPE, "Aragorn's Pipe", null, true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.aragorn;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if ((PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                || PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self))
                && PlayConditions.canSpot(game, game.getGameState().getCurrentSiteNumber()+1, PossessionClass.PIPE)
                && PlayConditions.canDiscardFromPlay(self, game, CardType.POSSESSION, PossessionClass.PIPEWEED)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION, PossessionClass.PIPEWEED));
            action.appendEffect(
                    new PlayNextSiteEffect(action, playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
