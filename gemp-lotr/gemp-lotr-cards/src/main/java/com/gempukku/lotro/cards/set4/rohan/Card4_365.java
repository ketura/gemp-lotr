package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.ExtraFilters;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.cards.effects.CheckLimitEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 2
 * Resistance: 6
 * Signet: Theoden
 * Game Text: Fellowship: Play a [ROHAN] possession on a [ROHAN] companion to heal that companion (limit once per turn).
 */
public class Card4_365 extends AbstractCompanion {
    public Card4_365() {
        super(2, 6, 2, Culture.ROHAN, Race.MAN, Signet.THÉODEN, "Theoden", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canPlayFromHand(playerId, game, Culture.ROHAN, CardType.POSSESSION, ExtraFilters.attachableTo(game, Culture.ROHAN, CardType.COMPANION))) {
            final Filter additionalAttachmentFilter = Filters.and(Culture.ROHAN, CardType.COMPANION);
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to play", game.getGameState().getHand(playerId),
                            Filters.and(
                                    Culture.ROHAN,
                                    CardType.POSSESSION,
                                    ExtraFilters.attachableTo(game, additionalAttachmentFilter)), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.iterator().next();
                                AttachPermanentAction attachPermanentAction = ((AbstractAttachable) selectedCard.getBlueprint()).getPlayCardAction(playerId, game, selectedCard, additionalAttachmentFilter, 0);
                                game.getActionsEnvironment().addActionToStack(attachPermanentAction);
                                action.appendEffect(
                                        new CheckLimitEffect(action, self, 1, Phase.FELLOWSHIP,
                                                new AppendHealTargetEffect(action, attachPermanentAction)));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    private static class AppendHealTargetEffect extends UnrespondableEffect {
        private CostToEffectAction _action;
        private AttachPermanentAction _attachPermanentAction;

        private AppendHealTargetEffect(CostToEffectAction action, AttachPermanentAction attachPermanentAction) {
            _action = action;
            _attachPermanentAction = attachPermanentAction;
        }

        @Override
        protected void doPlayEffect(LotroGame game) {
            _action.appendEffect(
                    new HealCharactersEffect(_action.getActionSource(), _attachPermanentAction.getTarget()));
        }
    }
}

