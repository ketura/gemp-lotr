package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class AbstractFollower extends AbstractPermanent {
    private int _strength;
    private int _vitality;
    private int _resistance;

    public AbstractFollower(Side side, int twilightCost, int strength, int vitality, int resistance, Culture culture, String name) {
        this(side, twilightCost, strength, vitality, resistance, culture, name, false);
    }

    public AbstractFollower(Side side, int twilightCost, int strength, int vitality, int resistance, Culture culture, String name, boolean unique) {
        super(side, twilightCost, CardType.FOLLOWER, culture, Zone.SUPPORT, name, unique);
        _strength = strength;
        _vitality = vitality;
        _resistance = resistance;
    }

    @Override
    public final Modifier getAlwaysOnModifier(PhysicalCard self) {
        return super.getAlwaysOnModifier(self);
    }

    @Override
    public final List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        if (_strength != 0)
            modifiers.add(new StrengthModifier(self, Filters.hasAttached(self), _strength));
        if (_vitality != 0)
            modifiers.add(new VitalityModifier(self, Filters.hasAttached(self), _vitality));
        if (_resistance != 0)
            modifiers.add(new ResistanceModifier(self, Filters.hasAttached(self), _resistance));

        List<? extends Modifier> extraModifiers = getNonBasicStatsModifiers(self);
        if (extraModifiers != null)
            modifiers.addAll(extraModifiers);

        return modifiers;
    }

    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return null;
    }

    protected abstract boolean canPayAidCost(LotroGame game, PhysicalCard self);

    protected abstract Effect getAidCost(LotroGame game, PhysicalCard self);

    protected Filterable getFollowerTarget(LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.owner(self.getOwner()), Filters.or(CardType.COMPANION, CardType.MINION));
    }

    @Override
    public final List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)
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
