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
    public AbstractAttachableFPPossession(int twilight, Culture culture, String name) {
        this(twilight, culture, name, false);
    }

    public AbstractAttachableFPPossession(int twilight, Culture culture, String name, boolean unique) {
        super(Side.FREE_PEOPLE, CardType.POSSESSION, twilight, culture, name, unique);
    }

    private void appendTransferPossessionAction(List<Action> actions, LotroGame game, PhysicalCard self, Filter validTargetFilter) {
        GameState gameState = game.getGameState();
        if (Filters.canSpot(gameState, game.getModifiersQuerying(), validTargetFilter)
                && gameState.getCurrentPhase() == Phase.FELLOWSHIP
                && self.getZone() == Zone.ATTACHED) {

            Filter vaildTransferFilter;

            LotroCardBlueprint attachedTo = self.getAttachedTo().getBlueprint();
            if (attachedTo.getCardType() == CardType.COMPANION) {
                vaildTransferFilter = Filters.and(validTargetFilter,
                        Filters.or(
                                Filters.type(CardType.COMPANION),
                                Filters.and(Filters.type(CardType.ALLY), Filters.siteNumber(gameState.getCurrentSiteNumber()))));
            } else if (attachedTo.getSiteNumber() == gameState.getCurrentSiteNumber()) {
                vaildTransferFilter = Filters.and(validTargetFilter,
                        Filters.or(
                                Filters.type(CardType.COMPANION),
                                Filters.and(Filters.type(CardType.ALLY), Filters.siteNumber(gameState.getCurrentSiteNumber()))));
            } else {
                vaildTransferFilter = Filters.and(validTargetFilter,
                        Filters.type(CardType.ALLY), Filters.siteNumber(attachedTo.getSiteNumber()));
            }

            actions.add(new TransferPermanentAction(self, game, vaildTransferFilter));
        }
    }


    @Override
    public final List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>(super.getPhaseActions(playerId, game, self));
        appendTransferPossessionAction(actions, game, self, getValidTargetFilter(playerId, game, self));
        return actions;
    }
}
