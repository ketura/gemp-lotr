package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

public class AbstractMinion extends AbstractLotroCardBlueprint {
    private int _twilightCost;
    private int _strength;
    private int _vitality;
    private int _site;

    public AbstractMinion(int twilightCost, int strength, int vitality, int site, Culture culture, String name) {
        this(twilightCost, strength, vitality, site, culture, name, false);
    }

    public AbstractMinion(int twilightCost, int strength, int vitality, int site, Culture culture, String name, boolean unique) {
        super(Side.SHADOW, CardType.MINION, culture, name, unique);
        _twilightCost = twilightCost;
        _strength = strength;
        _vitality = vitality;
        _site = site;
    }

    private void appendPlayMinionActions(List<Action> actions, LotroGame game, PhysicalCard self) {
        ModifiersQuerying modifiersQuerying = game.getModifiersQuerying();
        if (PlayConditions.canPlayMinionDuringShadow(game.getGameState(), modifiersQuerying, self)) {
            actions.add(getPlayMinionAction(game, self));
        }
    }

    @Override
    public final List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        if (checkPlayRequirements(playerId, game, self))
            appendPlayMinionActions(actions, game, self);

        List<? extends Action> extraActions = getExtraPhaseActions(playerId, game, self);
        if (extraActions != null)
            actions.addAll(extraActions);

        return actions;
    }

    protected boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return true;
    }

    protected Action getPlayMinionAction(LotroGame game, PhysicalCard self) {
        return new PlayPermanentAction(self, Zone.SHADOW_CHARACTERS);
    }

    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        return null;
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
