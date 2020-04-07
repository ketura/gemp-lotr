package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.PlayerReconcilesAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 3, Race.ELF);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return CardType.COMPANION;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
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
                        public void playEffect(LotroGame game) {
                            game.getActionsEnvironment().addActionToStack(
                                    new PlayerReconcilesAction(game, playerId));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
