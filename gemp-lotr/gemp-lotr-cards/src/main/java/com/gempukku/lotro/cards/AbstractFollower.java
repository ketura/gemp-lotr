package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

public abstract class AbstractFollower extends AbstractPermanent {
    public AbstractFollower(Side side, int twilightCost, Culture culture, String name) {
        this(side, twilightCost, culture, name, false);
    }

    public AbstractFollower(Side side, int twilightCost, Culture culture, String name, boolean unique) {
        super(side, twilightCost, CardType.FOLLOWER, culture, Zone.SUPPORT, name, unique);
    }

    protected abstract boolean canPayAidCost(LotroGame game, PhysicalCard self);

    protected abstract Effect getAidCost(LotroGame game, PhysicalCard self);

    protected Filterable getFollowerTarget(LotroGame game, PhysicalCard self) {
        return CardType.COMPANION;
    }

    @Override
    public final List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)
                && self.getZone() == Zone.SUPPORT
                && canPayAidCost(game, self)
                && PlayConditions.canSpot(game, getFollowerTarget(game, self))) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    getAidCost(game, self));
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose character to transfer follower to", getFollowerTarget(game, self)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new TransferPermanentEffect(self, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return getExtraOptionalAfterTriggers(playerId, game, effectResult, self);
    }

    protected List<OptionalTriggerAction> getExtraOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        return null;
    }
}
