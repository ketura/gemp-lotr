package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.DiscountEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

public class AbstractPermanent extends AbstractLotroCardBlueprint {
    private int _twilightCost;
    private Zone _playedToZone;

    public AbstractPermanent(Side side, int twilightCost, CardType cardType, Culture culture, Zone playedToZone, String name) {
        this(side, twilightCost, cardType, culture, playedToZone, name, false);
    }

    public AbstractPermanent(Side side, int twilightCost, CardType cardType, Culture culture, Zone playedToZone, String name, boolean unique) {
        super(side, cardType, culture, name, unique);
        _playedToZone = playedToZone;
        _twilightCost = twilightCost;
        if (playedToZone == Zone.SUPPORT)
            addKeyword(Keyword.SUPPORT_AREA);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayPermanentAction action = new PlayPermanentAction(self, _playedToZone, twilightModifier);
        DiscountEffect discountEffect = getDiscountEffect(action, playerId, game, self);
        if (discountEffect != null)
            action.setDiscountEffect(discountEffect);
        return action;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        twilightModifier -= getPotentialExtraPaymentDiscount(playerId, game, self);
        return PlayConditions.checkUniqueness(game.getGameState(), game.getModifiersQuerying(), self)
                && (getSide() != Side.SHADOW || PlayConditions.canPayForShadowCard(game, self, twilightModifier, ignoreRoamingPenalty));
    }

    @Override
    public final int getTwilightCost() {
        return _twilightCost;
    }

    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    protected int getPotentialExtraPaymentDiscount(String playerId, LotroGame game, PhysicalCard self) {
        // Always non-negative 
        return 0;
    }

    protected DiscountEffect getDiscountEffect(Action action, String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    protected Phase getExtraPlayableInPhase() {
        return null;
    }

    @Override
    public final List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, (getSide() == Side.FREE_PEOPLE) ? Phase.FELLOWSHIP : Phase.SHADOW, self)
                && checkPlayRequirements(playerId, game, self, 0, false))
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0, false));
        Phase extraPhase = getExtraPlayableInPhase();
        if (extraPhase != null)
            if (PlayConditions.canPlayCardDuringPhase(game, extraPhase, self)
                    && checkPlayRequirements(playerId, game, self, 0, false))
                return Collections.singletonList(getPlayCardAction(playerId, game, self, 0, false));


        return getExtraPhaseActions(playerId, game, self);
    }

    @Override
    public final List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (self.getZone().isInPlay())
            return getOptionalInPlayBeforeActions(playerId, game, effect, self);
        return null;
    }

    @Override
    public final List<? extends Action> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (self.getZone().isInPlay())
            return getOptionalInPlayAfterActions(playerId, game, effectResult, self);
        return null;
    }

    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        return null;
    }

    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        return null;
    }
}
