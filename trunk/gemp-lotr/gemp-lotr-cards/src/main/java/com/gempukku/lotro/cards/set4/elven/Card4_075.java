package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.actions.PlayerReconcilesAction;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession
 * Game Text: To play, spot 3 Elves. Bearer must be a companion. Regroup: Discard this possession to reconcile your hand.
 */
public class Card4_075 extends AbstractAttachableFPPossession {
    public Card4_075() {
        super(1, 0, 0, Culture.ELVEN, null, "Lembas");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, additionalAttachmentFilter, twilightModifier)
                && Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF)) >= 3;
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.type(CardType.COMPANION);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.REGROUP, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new AbstractSuccessfulEffect() {
                        @Override
                        public String getText(LotroGame game) {
                            return "Reconcile hand";
                        }

                        @Override
                        public Effect.Type getType() {
                            return null;
                        }

                        @Override
                        public Collection<? extends EffectResult> playEffect(LotroGame game) {
                            game.getActionsEnvironment().addActionToStack(
                                    new PlayerReconcilesAction(game, playerId));
                            return null;
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
