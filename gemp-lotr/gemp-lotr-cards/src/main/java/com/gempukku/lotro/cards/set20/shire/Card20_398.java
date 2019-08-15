package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.CheckPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.effects.IncrementPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Merry's Pipe
 * Possession • Pipe
 * Bearer must be a Hobbit.
 * Fellowship: If bearer is Merry, you may discard a pipeweed possession to heal every companion bearing a pipe (limit once per phase).
 * http://lotrtcg.org/coreset/shire/merryspipe(r2).jpg
 */
public class Card20_398 extends AbstractAttachableFPPossession {
    public Card20_398() {
        super(1, 0, 0, Culture.SHIRE, PossessionClass.PIPE, "Merry's Pipe", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.HOBBIT;
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.isActive(game, Filters.name("Merry"), Filters.hasAttached(self))
                && PlayConditions.canDiscardFromPlay(self, game, CardType.POSSESSION, Keyword.PIPEWEED)
        && PlayConditions.checkPhaseLimit(game,self, 1)) {
            final ActivateCardAction action= new ActivateCardAction(self);
            action.appendCost(
                    new IncrementPhaseLimitEffect(self, 1));
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION, Keyword.PIPEWEED));
            action.appendEffect(
                            new HealCharactersEffect(self, CardType.COMPANION, Filters.hasAttached(PossessionClass.PIPE)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
