package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.CheckPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Gimli's Pipe
 * Possession • Pipe
 * Bearer must be a Dwarf.
 * Skirmish: If bearer is Gimli, discard a pipeweed possession and spot X pipes to make a companion bearing a pipe
 * damage +X (limit once per phase).
 * http://www.lotrtcg.org/coreset/dwarven/gimlispipe(r2).jpg
 */
public class Card20_056 extends AbstractAttachableFPPossession {
    public Card20_056() {
        super(1, 0, 0, Culture.DWARVEN, PossessionClass.PIPE, "Gimli's Pipe", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.isActive(game, Filters.gimli, Filters.hasAttached(self))
                && PlayConditions.canDiscardFromPlay(self, game, CardType.POSSESSION, Keyword.PIPEWEED)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION, Keyword.PIPEWEED));
            action.appendCost(
                    new ForEachYouSpotEffect(playerId, PossessionClass.PIPE) {
                        @Override
                        protected void spottedCards(final int spotCount) {
                            action.appendEffect(
                                    new CheckPhaseLimitEffect(action, self, 1,
                                            new ChooseActiveCardEffect(self, playerId, "Choose companion bearing a pipe", CardType.COMPANION, Filters.hasAttached(PossessionClass.PIPE)) {
                                                @Override
                                                protected void cardSelected(LotroGame game, PhysicalCard card) {
                                                    action.appendEffect(
                                                            new AddUntilEndOfPhaseModifierEffect(
                                                                    new KeywordModifier(self, card, Keyword.DAMAGE, spotCount)));
                                                }
                                            }));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
