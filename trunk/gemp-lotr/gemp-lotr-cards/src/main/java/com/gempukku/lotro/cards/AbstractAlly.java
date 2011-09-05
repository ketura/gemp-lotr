package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
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

public class AbstractAlly extends AbstractLotroCardBlueprint {
    private int _twilight;
    private int _siteNumber;
    private int _strength;
    private int _vitality;

    public AbstractAlly(int twilight, int siteNumber, int strength, int vitality, Culture culture, String name) {
        this(twilight, siteNumber, strength, vitality, culture, name, false);
    }

    public AbstractAlly(int twilight, int siteNumber, int strength, int vitality, Culture culture, String name, boolean unique) {
        super(Side.FREE_PEOPLE, CardType.ALLY, culture, name, unique);
        _twilight = twilight;
        _siteNumber = siteNumber;
        _strength = strength;
        _vitality = vitality;
    }

    @Override
    public final List<? extends Action> getInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        if (checkPlayRequirements(playerId, game, self))
            appendPlayAllyActions(actions, game, self);

        appendHealAllyActions(actions, game, self);

        List<? extends Action> extraActions = getExtraPhaseActions(playerId, game, self);
        if (extraActions != null)
            actions.addAll(extraActions);

        return actions;
    }

    private void appendPlayAllyActions(List<Action> actions, LotroGame game, PhysicalCard self) {
        ModifiersQuerying modifiersQuerying = game.getModifiersQuerying();
        if (PlayConditions.canPlayCharacterDuringFellowship(game.getGameState(), modifiersQuerying, self)) {
            actions.add(getPlayAllyAction(game, self));
        }
    }

    private void appendHealAllyActions(List<Action> actions, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canHealByDiscarding(game.getGameState(), game.getModifiersQuerying(), self)) {
            CostToEffectAction action = new CostToEffectAction(self, null, "Discard card to heal");
            action.addCost(new DiscardCardFromHandEffect(self));

            PhysicalCard active = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name(self.getBlueprint().getName()));
            if (active != null)
                action.addEffect(new HealCharacterEffect(active));

            actions.add(action);
        }
    }

    protected boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return true;
    }

    protected Action getPlayAllyAction(LotroGame game, PhysicalCard self) {
        return new PlayPermanentAction(self, Zone.FREE_SUPPORT);
    }

    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        return null;
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

    @Override
    public int getResistance() {
        return 0;
    }
}
