package com.gempukku.lotro.logic.cardtype;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.TransferPermanentEffect;
import com.gempukku.lotro.logic.timing.*;

import java.util.Collections;
import java.util.List;

public abstract class AbstractFollower extends AbstractPermanent {
    private int _strength;
    private int _vitality;
    private int _resistance;

    public AbstractFollower(Side side, int twilightCost, int strength, int vitality, int resistance, Culture culture, String name) {
        this(side, twilightCost, strength, vitality, resistance, culture, name, null, false);
    }

    public AbstractFollower(Side side, int twilightCost, int strength, int vitality, int resistance, Culture culture, String name, String subTitle, boolean unique) {
        super(side, twilightCost, CardType.FOLLOWER, culture, name, subTitle, unique);
        _strength = strength;
        _vitality = vitality;
        _resistance = resistance;
    }

    protected abstract boolean canPayAidCost(LotroGame game, PhysicalCard self);

    protected abstract Effect getAidCost(LotroGame game, Action action, PhysicalCard self);

    protected Filterable getFollowerTarget(LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.owner(self.getOwner()), Filters.or(CardType.COMPANION, CardType.MINION));
    }

    @Override
    public final List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)
                && canPayAidCost(game, self)
                && PlayConditions.isActive(game, getFollowerTarget(game, self))) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    getAidCost(game, action, self));
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose character to transfer follower to", getFollowerTarget(game, self), Filters.not(Filters.hasAttached(self))) {
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

    @Override
    public int getStrength() {
        return _strength;
    }

    @Override
    public int getVitality() {
        return _vitality;
    }

    @Override
    public int getResistance() {
        return _resistance;
    }
}
