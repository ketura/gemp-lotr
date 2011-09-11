package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.actions.TransferPermanentAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractAttachableFPPossession extends AbstractAttachable {
    public AbstractAttachableFPPossession(int twilight, Culture culture, Keyword possessionClass, String name) {
        this(twilight, culture, possessionClass, name, false);
    }

    public AbstractAttachableFPPossession(int twilight, Culture culture, Keyword possessionClass, String name, boolean unique) {
        super(Side.FREE_PEOPLE, CardType.POSSESSION, twilight, culture, possessionClass, name, unique);
    }

    private void appendTransferPossessionAction(List<Action> actions, LotroGame game, PhysicalCard self, Filter validTargetFilter) {
        GameState gameState = game.getGameState();
        if (Filters.canSpot(gameState, game.getModifiersQuerying(), validTargetFilter)
                && gameState.getCurrentPhase() == Phase.FELLOWSHIP
                && self.getZone() == Zone.ATTACHED) {

            Filter validTransferFilter;

            LotroCardBlueprint attachedTo = self.getAttachedTo().getBlueprint();
            if (attachedTo.getCardType() == CardType.COMPANION) {
                validTransferFilter = Filters.and(validTargetFilter,
                        Filters.or(
                                Filters.type(CardType.COMPANION),
                                Filters.and(Filters.type(CardType.ALLY), Filters.siteNumber(gameState.getCurrentSiteNumber()))));
            } else if (attachedTo.getSiteNumber() == gameState.getCurrentSiteNumber()) {
                validTransferFilter = Filters.and(validTargetFilter,
                        Filters.or(
                                Filters.type(CardType.COMPANION),
                                Filters.and(Filters.type(CardType.ALLY), Filters.siteNumber(gameState.getCurrentSiteNumber()))));
            } else {
                validTransferFilter = Filters.and(validTargetFilter,
                        Filters.type(CardType.ALLY), Filters.siteNumber(attachedTo.getSiteNumber()));
            }

            if (Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), validTransferFilter))
                actions.add(new TransferPermanentAction(self, game, validTransferFilter));
        }
    }

    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    protected final List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();
        appendTransferPossessionAction(actions, game, self, getFullValidTargetFilter(playerId, game, self));
        List<? extends Action> extraActions = getExtraInPlayPhaseActions(playerId, game, self);
        if (extraActions != null)
            actions.addAll(extraActions);
        return actions;
    }
}
