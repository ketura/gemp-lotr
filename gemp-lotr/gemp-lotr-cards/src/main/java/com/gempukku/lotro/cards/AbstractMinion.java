package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.actions.PlayPermanentFromHandAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.List;

public class AbstractMinion extends AbstractLotroCardBlueprint {
    private int _twilightCost;
    private int _strength;
    private int _vitality;
    private int _site;

    public AbstractMinion(int twilightCost, int strength, int vitality, int site, Culture culture, String name, String blueprintId) {
        this(twilightCost, strength, vitality, site, culture, name, blueprintId, false);
    }

    public AbstractMinion(int twilightCost, int strength, int vitality, int site, Culture culture, String name, String blueprintId, boolean unique) {
        super(Side.SHADOW, CardType.MINION, culture, name, blueprintId, unique);
        _twilightCost = twilightCost;
        _strength = strength;
        _vitality = vitality;
        _site = site;
    }

    protected void appendPlayMinionAction(List<Action> actions, LotroGame lotroGame, PhysicalCard self) {
        ModifiersQuerying modifiersQuerying = lotroGame.getModifiersQuerying();
        if (PlayConditions.canPlayMinionDuringShadow(lotroGame.getGameState(), modifiersQuerying, self)) {
            actions.add(new PlayPermanentFromHandAction(self, Zone.SHADOW_CHARACTERS));
        }
    }

    @Override
    public int getTwilightCost() {
        return _twilightCost;
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
    public int getSiteNumber() {
        return _site;
    }
}
