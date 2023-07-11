package com.gempukku.lotro.game.effects.choose;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.GameUtils;
import com.gempukku.lotro.game.effects.AssignmentEffect;
import com.gempukku.lotro.game.actions.SubAction;
import com.gempukku.lotro.game.decisions.CardsSelectionDecision;
import com.gempukku.lotro.game.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.game.timing.AbstractSubActionEffect;
import com.gempukku.lotro.game.actions.Action;

import java.util.Collection;

public class ChooseAndDoAssignmentEffect extends AbstractSubActionEffect {
    private final Action _action;
    private final String _playerId;
    private final Filterable _minionFilter;
    private final Filterable _fpFilter;

    public ChooseAndDoAssignmentEffect(Action action, String playerId, Filterable minionFilter, Filterable fpFilter) {
        _action = action;
        _playerId = playerId;
        _minionFilter = minionFilter;
        _fpFilter = fpFilter;
    }

    @Override
    public String getText(DefaultGame game) {
        return "Assign minion to free people's character";
    }

    @Override
    public Type getType() {
        return null;
    }

    private Side getDecidingPlayerSide(DefaultGame game) {
        return game.getGameState().getCurrentPlayerId().equals(_playerId) ? Side.FREE_PEOPLE : Side.SHADOW;
    }

    @Override
    public boolean isPlayableInFull(DefaultGame game) {
        return getAssignableMinions(game).size() > 0;
    }

    private Collection<PhysicalCard> getAssignableMinions(DefaultGame game) {
        return Filters.filterActive(game, CardType.MINION, _minionFilter, Filters.assignableToSkirmishAgainst(getDecidingPlayerSide(game), _fpFilter, false, false));
    }

    @Override
    public void playEffect(final DefaultGame game) {
        Collection<PhysicalCard> assignableMinions = getAssignableMinions(game);
        if (assignableMinions.size() > 0) {
            if (assignableMinions.size() == 1)
                minionSelected(game, assignableMinions.iterator().next());
            else
                game.getUserFeedback().sendAwaitingDecision(_playerId,
                        new CardsSelectionDecision(1, "Choose minion to assign", assignableMinions, 1, 1) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                minionSelected(game, getSelectedCardsByResponse(result).iterator().next());
                            }
                        });
        }
    }

    private void minionSelected(final DefaultGame game, final PhysicalCard minion) {
        Collection<PhysicalCard> assignableFpCharacters = Filters.filterActive(game, Filters.or(CardType.COMPANION, CardType.ALLY), _fpFilter, Filters.assignableToSkirmishAgainst(getDecidingPlayerSide(game), minion, false, false));
        if (assignableFpCharacters.size() == 1)
            assignmentSelected(game, assignableFpCharacters.iterator().next(), minion);
        else
            game.getUserFeedback().sendAwaitingDecision(_playerId,
                    new CardsSelectionDecision(1, "Choose character to assign " + GameUtils.getCardLink(minion) + " to", assignableFpCharacters, 1, 1) {
                        @Override
                        public void decisionMade(String result) throws DecisionResultInvalidException {
                            assignmentSelected(game, getSelectedCardsByResponse(result).iterator().next(), minion);
                        }
                    });
    }

    private void assignmentSelected(DefaultGame game, PhysicalCard fpCharacter, PhysicalCard minion) {
        SubAction subAction = new SubAction(_action);
        subAction.appendEffect(
                new AssignmentEffect(_playerId, fpCharacter, minion, false));
        processSubAction(game, subAction);
    }
}
