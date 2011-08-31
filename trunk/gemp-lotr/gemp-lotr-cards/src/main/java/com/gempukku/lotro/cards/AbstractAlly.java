package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.HealCharacterEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardCardFromHandEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.List;

public class AbstractAlly extends AbstractLotroCardBlueprint {
    private int _twilight;
    private int _siteNumber;
    private int _strength;
    private int _vitality;

    public AbstractAlly(int twilight, int siteNumber, int strength, int vitality, Culture culture, String name, String blueprintId) {
        this(twilight, siteNumber, strength, vitality, culture, name, blueprintId, false);
    }

    public AbstractAlly(int twilight, int siteNumber, int strength, int vitality, Culture culture, String name, String blueprintId, boolean unique) {
        super(Side.FREE_PEOPLE, CardType.ALLY, culture, name, blueprintId, unique);
        _twilight = twilight;
        _siteNumber = siteNumber;
        _strength = strength;
        _vitality = vitality;
    }

    protected void appendPlayAllyActions(List<Action> actions, LotroGame lotroGame, PhysicalCard self) {
        ModifiersQuerying modifiersQuerying = lotroGame.getModifiersQuerying();
        if (PlayConditions.canPlayCharacterDuringFellowship(lotroGame.getGameState(), modifiersQuerying, self)) {
            actions.add(new PlayPermanentAction(self, Zone.FREE_SUPPORT));
        }
    }

    protected void appendHealAllyActions(List<Action> actions, LotroGame lotroGame, PhysicalCard self) {
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
    public int getTwilightCost() {
        return _twilight;
    }

    @Override
    public int getSiteNumber() {
        return _siteNumber;
    }

    @Override
    public int getStrength() {
        return _strength;
    }

    @Override
    public int getVitality() {
        return _vitality;
    }
}
