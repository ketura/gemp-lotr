package com.gempukku.lotro.cards.effects.choose;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.AbstractEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Set;

public class ChooseAndTransferAttachableEffect extends AbstractEffect {
    private Action _action;
    private String _playerId;
    private Filterable _attachedTo;
    private Filterable _attachedCard;
    private Filterable _transferTo;

    public ChooseAndTransferAttachableEffect(Action action, String playerId, Filterable attachedCard, Filterable attachedTo, Filterable transferTo) {
        _action = action;
        _playerId = playerId;
        _attachedCard = attachedCard;
        _attachedTo = attachedTo;
        _transferTo = transferTo;
    }

    @Override
    public String getText(LotroGame game) {
        return null;
    }

    @Override
    public Type getType() {
        return null;
    }

    protected Filterable getValidTargetFilter(LotroGame game, final PhysicalCard attachment, AbstractAttachable attachable) {
        return Filters.and(
                _transferTo,
                attachable.getFullValidTargetFilter(attachment.getOwner(), game, attachment),
                Filters.not(attachment.getAttachedTo()),
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard target) {
                        return modifiersQuerying.canHaveTransferredOn(gameState, attachment, target);
                    }
                });
    }

    protected Collection<PhysicalCard> getPossibleAttachmentsToTransfer(final LotroGame game) {
        return Filters.filterActive(game.getGameState(), game.getModifiersQuerying(),
                _attachedCard,
                Filters.attachedTo(_attachedTo),
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, final PhysicalCard transferredCard) {
                        if (!(transferredCard.getBlueprint() instanceof AbstractAttachable))
                            return false;

                        if (!modifiersQuerying.canBeTransferred(gameState, transferredCard))
                            return false;

                        AbstractAttachable attachable = (AbstractAttachable) transferredCard.getBlueprint();
                        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), getValidTargetFilter(game, transferredCard, attachable));
                    }
                });
    }

    @Override
    public boolean isPlayableInFull(LotroGame game) {
        return getPossibleAttachmentsToTransfer(game).size() > 0;
    }

    @Override
    protected FullEffectResult playEffectReturningResult(final LotroGame game) {
        final Collection<PhysicalCard> possibleAttachmentsToTransfer = getPossibleAttachmentsToTransfer(game);
        if (possibleAttachmentsToTransfer.size() > 0) {
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, "Choose card to transfer", possibleAttachmentsToTransfer, 1, 1) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            final Set<PhysicalCard> selectedAttachments = getSelectedCardsByResponse(result);
                            if (selectedAttachments.size() == 1) {
                                final PhysicalCard attachment = selectedAttachments.iterator().next();
                                final PhysicalCard transferredFrom = attachment.getAttachedTo();
                                AbstractAttachable attachable = (AbstractAttachable) attachment.getBlueprint();
                                final Collection<PhysicalCard> validTargets = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), getValidTargetFilter(game, attachment, attachable));
                                game.getUserFeedback().sendAwaitingDecision(
                                        _playerId,
                                        new CardsSelectionDecision(1, "Choose transfer target", validTargets, 1, 1) {
                                            @Override
                                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                                final Set<PhysicalCard> selectedTargets = getSelectedCardsByResponse(result);
                                                if (selectedTargets.size() == 1) {
                                                    final PhysicalCard selectedTarget = selectedTargets.iterator().next();
                                                    SubAction subAction = new SubAction(_action);
                                                    subAction.appendEffect(
                                                            new TransferPermanentEffect(attachment, selectedTarget) {
                                                                @Override
                                                                protected void afterTransferredCallback() {
                                                                    afterTransferCallback(attachment, transferredFrom, selectedTarget);
                                                                }
                                                            });
                                                    game.getActionsEnvironment().addActionToStack(subAction);
                                                }
                                            }
                                        }
                                );
                            }
                        }
                    });
        }
        return new FullEffectResult(false, false);
    }

    protected void afterTransferCallback(PhysicalCard transferredCard, PhysicalCard transferredFrom, PhysicalCard transferredTo) {

    }
}
