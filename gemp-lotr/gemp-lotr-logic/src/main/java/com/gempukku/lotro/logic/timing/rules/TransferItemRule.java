package com.gempukku.lotro.logic.timing.rules;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersLogic;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;

import java.util.List;

public class TransferItemRule {
    private ModifiersLogic modifiersLogic;

    public TransferItemRule(ModifiersLogic modifiersLogic) {
        this.modifiersLogic = modifiersLogic;
    }

    public void applyRule() {
        modifiersLogic.addAlwaysOnModifier(
                new AbstractModifier(null, "Transfer item",
                        Filters.and(Side.FREE_PEOPLE, Filters.inPlay(), Zone.ATTACHED, Filters.or(CardType.POSSESSION, CardType.ARTIFACT)),
                        new PhaseCondition(Phase.FELLOWSHIP), ModifierEffect.EXTRA_ACTION_MODIFIER) {
                    @Override
                    public List<? extends ActivateCardAction> getExtraPhaseAction(LotroGame game, PhysicalCard card) {
//                        if (game.getModifiersQuerying().canBeTransferred(game, card)) {
//                            final AbstractAttachableFPPossession attachable = (AbstractAttachableFPPossession) card.getBlueprint();
//                            attachable.getFullValidTargetFilter(card.getOwner(), game)
//                            if (Filters.countActive(game, modifiersQuerying, validTargetFilter)>0
//                                    && gameState.getCurrentPhase() == Phase.FELLOWSHIP
//                                    && self.getZone() == Zone.ATTACHED) {
//
//                                Filter validTransferFilter;
//
//                                PhysicalCard attachedToCard = self.getAttachedTo();
//                                LotroCardBlueprint attachedTo = attachedToCard.getBlueprint();
//                                if (attachedTo.getCardType() == CardType.COMPANION) {
//                                    validTransferFilter = Filters.and(validTargetFilter,
//                                            Filters.or(
//                                                    CardType.COMPANION,
//                                                    Filters.allyAtHome));
//                                } else if (attachedTo.isAllyAtHome(gameState.getCurrentSiteNumber(), gameState.getCurrentSiteBlock())) {
//                                    validTransferFilter = Filters.and(validTargetFilter,
//                                            Filters.or(
//                                                    CardType.COMPANION,
//                                                    Filters.allyWithSameHome(attachedToCard)));
//                                } else {
//                                    validTransferFilter = Filters.and(validTargetFilter,
//                                            Filters.allyWithSameHome(attachedToCard));
//                                }
//
//                                validTransferFilter = Filters.and(validTransferFilter,
//                                        Filters.not(self.getAttachedTo()),
//                                        new Filter() {
//                                            @Override
//                                            public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
//                                                return game.getModifiersQuerying().canHaveTransferredOn(game, self, physicalCard);
//                                            }
//                                        });
//
//                                if (Filters.countActive(game, validTransferFilter)>0)
//                                    actions.add(new TransferPermanentAction(self, validTransferFilter));
//                            }
//                        }
//                            appendTransferPossessionAction(actions, game, self, getFullValidTargetFilter(playerId, game, self));
                        return null;
                    }
                });
    }
}
