package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class AbstractAttachable extends AbstractLotroCardBlueprint {
    private int _twilight;

    public AbstractAttachable(Side side, CardType cardType, int twilight, Culture culture, String name) {
        this(side, cardType, twilight, culture, name, false);
    }

    public AbstractAttachable(Side side, CardType cardType, int twilight, Culture culture, String name, boolean unique) {
        super(side, cardType, culture, name, unique);
        _twilight = twilight;
    }

    protected abstract Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self);

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.checkUniqueness(game.getGameState(), game.getModifiersQuerying(), self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), getValidTargetFilter(playerId, game, self));
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();
        Side side = self.getBlueprint().getSide();
        if (((side == Side.FREE_PEOPLE && PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self))
                || (side == Side.SHADOW && PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)))
                && checkPlayRequirements(playerId, game, self, 0)) {
            actions.add(getPlayCardAction(playerId, game, self, 0));
        }

        List<? extends Action> extraPhaseActions = getExtraPhaseActions(playerId, game, self);
        if (extraPhaseActions != null)
            actions.addAll(extraPhaseActions);

        return actions;
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new AttachPermanentAction(game, self, getValidTargetFilter(playerId, game, self), getAttachCostModifiers(playerId, game, self));
    }

    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    protected Map<Filter, Integer> getAttachCostModifiers(String playerId, LotroGame game, PhysicalCard self) {
        return Collections.emptyMap();
    }

    @Override
    public int getTwilightCost() {
        return _twilight;
    }
}
