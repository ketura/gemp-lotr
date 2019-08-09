package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.CheckPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
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
 * Possession •  Pipe
 * Bearer must be a [Gondor] Man.
 * Skirmish: If bearer is Aragorn, discard a pipeweed possession and spot X pipes to make a companion bearing
 * a pipe strength +X (limit once per phase).
 * http://www.lotrtcg.org/coreset/gondor/aragornspipe(r2).jpg
 */
public class Card20_178 extends AbstractAttachableFPPossession {
    public Card20_178() {
        super(1, 0, 0, Culture.GONDOR, PossessionClass.PIPE, "Aragorn's Pipe", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.isActive(game, Filters.aragorn, Filters.hasAttached(self))
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
                                            new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, spotCount, CardType.COMPANION, Filters.hasAttached(PossessionClass.PIPE))));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
