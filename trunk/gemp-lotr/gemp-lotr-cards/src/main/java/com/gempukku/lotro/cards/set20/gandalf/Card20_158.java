package com.gempukku.lotro.cards.set20.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CheckPhaseLimitEffect;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Gandalf's Pipe
 * Possession • Pipe
 * Bearer must be a Wizard.
 * Maneuver: If bearer is Gandalf, discard a pipeweed possession and spot X pipes to draw X cards (limit once per phase).
 * http://www.lotrtcg.org/coreset/gandalf/gandalfspipe(r2).jpg
 */
public class Card20_158 extends AbstractAttachableFPPossession {
    public Card20_158() {
        super(1, 0, 0, Culture.GANDALF, PossessionClass.PIPE, "Gandalf's Pipe", null, true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.WIZARD;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.isActive(game, Filters.gandalf, Filters.hasAttached(self))
                && PlayConditions.canDiscardFromPlay(self, game, CardType.POSSESSION, Keyword.PIPEWEED)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION, Keyword.PIPEWEED));
            action.appendCost(
                    new ForEachYouSpotEffect(playerId, PossessionClass.PIPE) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            action.appendEffect(
                                    new CheckPhaseLimitEffect(action, self, 1,
                                            new DrawCardsEffect(action, playerId, spotCount)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
