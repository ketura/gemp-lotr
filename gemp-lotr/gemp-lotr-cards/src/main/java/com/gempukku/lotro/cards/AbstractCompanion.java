package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardCardFromHandEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractCompanion extends AbstractLotroCardBlueprint {
    private int _twilightCost;
    private int _strength;
    private int _vitality;
    private Signet _signet;

    public AbstractCompanion(int twilightCost, int strength, int vitality, Culture culture, String name) {
        this(twilightCost, strength, vitality, culture, name, false);
    }

    public AbstractCompanion(int twilightCost, int strength, int vitality, Culture culture, String name, boolean unique) {
        super(Side.FREE_PEOPLE, CardType.COMPANION, culture, name, unique);
        _twilightCost = twilightCost;
        _strength = strength;
        _vitality = vitality;
    }

    @Override
    public Signet getSignet() {
        return _signet;
    }

    protected void setSignet(Signet signet) {
        _signet = signet;
    }

    protected void appendPlayCompanionActions(List<Action> actions, LotroGame lotroGame, PhysicalCard self) {
        ModifiersQuerying modifiersQuerying = lotroGame.getModifiersQuerying();
        if (PlayConditions.canPlayCompanionDuringSetup(lotroGame.getGameState(), modifiersQuerying, self)
                || PlayConditions.canPlayCharacterDuringFellowship(lotroGame.getGameState(), modifiersQuerying, self)) {
            actions.add(new PlayPermanentAction(self, Zone.FREE_CHARACTERS));
        }
    }

    protected void appendHealCompanionActions(List<Action> actions, LotroGame lotroGame, PhysicalCard self) {
        if (PlayConditions.canHealByDiscarding(lotroGame.getGameState(), lotroGame.getModifiersQuerying(), self)) {
            CostToEffectAction action = new CostToEffectAction(self, null, "Discard card to heal");
            action.addCost(new DiscardCardFromHandEffect(self));

            PhysicalCard active = Filters.findFirstActive(lotroGame.getGameState(), lotroGame.getModifiersQuerying(), Filters.name(self.getBlueprint().getName()));
            if (active != null)
                action.addEffect(new HealCharacterEffect(active));

            actions.add(action);
        }
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();
        appendPlayCompanionActions(actions, game, self);
        appendHealCompanionActions(actions, game, self);
        return actions;
    }

    @Override
    public final int getTwilightCost() {
        return _twilightCost;
    }

    @Override
    public final int getStrength() {
        return _strength;
    }

    @Override
    public final int getVitality() {
        return _vitality;
    }

    @Override
    public int getResistance() {
        return 6;
    }
}
