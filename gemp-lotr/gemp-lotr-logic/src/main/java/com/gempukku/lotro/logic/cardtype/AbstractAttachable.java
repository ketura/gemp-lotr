package com.gempukku.lotro.logic.cardtype;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.AttachPermanentAction;
import com.gempukku.lotro.logic.timing.*;

import java.util.*;

public abstract class AbstractAttachable extends AbstractLotroCardBlueprint {
    private PossessionClass _possessionClass;

    public AbstractAttachable(Side side, CardType cardType, int twilight, Culture culture, PossessionClass possessionClass, String name) {
        this(side, cardType, twilight, culture, possessionClass, name, null, false);
    }

    public AbstractAttachable(Side side, CardType cardType, int twilight, Culture culture, PossessionClass possessionClass, String name, String subTitle, boolean unique) {
        super(twilight, side, cardType, culture, name, subTitle, unique);
        _possessionClass = possessionClass;
    }

    @Override
    public Set<PossessionClass> getPossessionClasses() {
        return Collections.singleton(_possessionClass);
    }

    private Filter getFullAttachValidTargetFilter(String playerId, final LotroGame game, final PhysicalCard self) {
        return Filters.and(RuleUtils.getFullValidTargetFilter(playerId, game, self),
                new Filter() {
                    @Override
                    public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                        return game.getModifiersQuerying().canHavePlayedOn(game, self, physicalCard);
                    }
                });
    }

    @Override
    public final boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return checkPlayRequirements(playerId, game, self, withTwilightRemoved, Filters.any, twilightModifier);
    }

    protected boolean skipUniquenessCheck() {
        return false;
    }

    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, Filter additionalAttachmentFilter, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, false, false)
                && (skipUniquenessCheck() || PlayConditions.checkUniqueness(game, self, false))
                && Filters.countActive(game, getFullAttachValidTargetFilter(playerId, game, self), additionalAttachmentFilter)>0;
    }

    @Override
    public final List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();
        final Phase phase = (getSide() == Side.FREE_PEOPLE) ? Phase.FELLOWSHIP : Phase.SHADOW;
        if (PlayConditions.canPlayCardDuringPhase(game, phase, self)
                && checkPlayRequirements(playerId, game, self, 0, 0, false, false)) {
            actions.add(getPlayCardAction(playerId, game, self, 0, false));
        }

        List<? extends Action> extraPhaseActions = getExtraPhaseActions(playerId, game, self);
        if (extraPhaseActions != null)
            actions.addAll(extraPhaseActions);

        return actions;
    }

    @Override
    public final AttachPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return getPlayCardAction(playerId, game, self, Filters.any, twilightModifier);
    }

    public AttachPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, Filterable additionalAttachmentFilter, int twilightModifier) {
        return new AttachPermanentAction(game, self, Filters.and(getFullAttachValidTargetFilter(playerId, game, self), additionalAttachmentFilter), getAttachCostModifiers(playerId, game, self), twilightModifier);
    }

    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    protected Map<Filter, Integer> getAttachCostModifiers(String playerId, LotroGame game, PhysicalCard self) {
        return Collections.emptyMap();
    }

    @Override
    public final List<? extends Action> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (self.getZone().isInPlay())
            return getOptionalInPlayAfterActions(playerId, game, effectResult, self);
        return null;
    }

    @Override
    public final List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (self.getZone().isInPlay())
            return getOptionalInPlayBeforeActions(playerId, game, effect, self);
        return null;
    }

    public List<? extends Action> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }

    public List<? extends Action> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        return null;
    }
}
