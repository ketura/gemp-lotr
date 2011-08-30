package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.LotroCardBlueprint;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class AbstractAttachable extends AbstractLotroCardBlueprint {
    private int _twilight;

    public AbstractAttachable(Side side, CardType cardType, int twilight, Culture culture, String name, String blueprintId) {
        this(side, cardType, twilight, culture, name, blueprintId, false);
    }

    public AbstractAttachable(Side side, CardType cardType, int twilight, Culture culture, String name, String blueprintId, boolean unique) {
        super(side, cardType, culture, name, blueprintId, unique);
        _twilight = twilight;
    }

    protected void appendAttachCardAction(List<Action> actions, LotroGame game, PhysicalCard self, Filter validTargetFilter) {
        appendAttachCardAction(actions, game, self, validTargetFilter, Collections.<Filter, Integer>emptyMap());
    }

    protected void appendAttachCardAction(List<Action> actions, LotroGame game, PhysicalCard self, Filter validTargetFilter, Map<Filter, Integer> playCostModifiers) {
        GameState gameState = game.getGameState();
        LotroCardBlueprint blueprint = self.getBlueprint();
        if (gameState.getCurrentPhase() == ((getSide() == Side.FREE_PEOPLE) ? Phase.FELLOWSHIP : Phase.SHADOW)
                && (self.getZone() == Zone.HAND || self.getZone() == Zone.DECK)
                && (!blueprint.isUnique() || !Filters.canSpot(gameState, game.getModifiersQuerying(), Filters.name(blueprint.getName())))
                && Filters.canSpot(gameState, game.getModifiersQuerying(), validTargetFilter)) {

            actions.add(new AttachPermanentAction(self, validTargetFilter, playCostModifiers));
        }
    }

    @Override
    public int getTwilightCost() {
        return _twilight;
    }
}
