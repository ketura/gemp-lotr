package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.actions.AttachPermanentAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.*;

public abstract class AbstractAttachable extends AbstractLotroCardBlueprint {
    private int _twilight;
    private PossessionClass _possessionClass;

    public AbstractAttachable(Side side, CardType cardType, int twilight, Culture culture, PossessionClass possessionClass, String name) {
        this(side, cardType, twilight, culture, possessionClass, name, false);
    }

    public AbstractAttachable(Side side, CardType cardType, int twilight, Culture culture, PossessionClass possessionClass, String name, boolean unique) {
        super(side, cardType, culture, name, unique);
        _twilight = twilight;
        _possessionClass = possessionClass;
    }

    public PossessionClass getPossessionClass() {
        return _possessionClass;
    }

    public boolean isExtraPossessionClass() {
        return false;
    }

    protected Filter getFullValidTargetFilter(String playerId, final LotroGame game, final PhysicalCard self) {
        return Filters.and(getValidTargetFilter(playerId, game, self),
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        PossessionClass possessionClass = getPossessionClass();
                        if (possessionClass != null) {
                            boolean extraPossessionClass = isExtraPossessionClass();
                            List<PhysicalCard> attachedCards = game.getGameState().getAttachedCards(physicalCard);
                            Collection<PhysicalCard> matchingClassPossessions = Filters.filter(attachedCards, gameState, modifiersQuerying, Filters.or(Filters.type(CardType.POSSESSION), Filters.type(CardType.ARTIFACT)), Filters.possessionClass(possessionClass));
                            if (matchingClassPossessions.size() > 1)
                                return false;
                            if (!extraPossessionClass && matchingClassPossessions.size() == 1 &&
                                    !((AbstractAttachable) matchingClassPossessions.iterator().next().getBlueprint()).isExtraPossessionClass())
                                return false;
                        }
                        return true;
                    }
                });
    }

    private Filter getFullAttachValidTargetFilter(String playerId, final LotroGame game, final PhysicalCard self) {
        return Filters.and(getFullValidTargetFilter(playerId, game, self),
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return modifiersQuerying.canHavePlayedOn(gameState, self, physicalCard);
                    }
                });
    }

    protected abstract Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self);

    @Override
    public final boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return checkPlayRequirements(playerId, game, self, Filters.any, twilightModifier);
    }

    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return (self.getBlueprint().getSide() != Side.SHADOW || PlayConditions.canPayForShadowCard(game, self, twilightModifier))
                && PlayConditions.checkUniqueness(game.getGameState(), game.getModifiersQuerying(), self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), getFullAttachValidTargetFilter(playerId, game, self), additionalAttachmentFilter);
    }

    @Override
    public final List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();
        Side side = self.getBlueprint().getSide();
        if (((side == Side.FREE_PEOPLE && PlayConditions.canPlayCardDuringPhase(game, Phase.FELLOWSHIP, self))
                || (side == Side.SHADOW && PlayConditions.canPlayCardDuringPhase(game, Phase.SHADOW, self)))
                && checkPlayRequirements(playerId, game, self, 0)) {
            actions.add(getPlayCardAction(playerId, game, self, 0));
        }

        List<? extends Action> extraPhaseActions = getExtraPhaseActions(playerId, game, self);
        if (extraPhaseActions != null)
            actions.addAll(extraPhaseActions);

        return actions;
    }

    @Override
    public final AttachPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return getPlayCardAction(playerId, game, self, Filters.any, twilightModifier);
    }

    public AttachPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, Filter additionalAttachmentFilter, int twilightModifier) {
        return new AttachPermanentAction(game, self, Filters.and(getFullAttachValidTargetFilter(playerId, game, self), additionalAttachmentFilter), getAttachCostModifiers(playerId, game, self), twilightModifier);
    }

    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    protected Map<Filter, Integer> getAttachCostModifiers(String playerId, LotroGame game, PhysicalCard self) {
        return Collections.emptyMap();
    }

    @Override
    public final int getTwilightCost() {
        return _twilight;
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
