package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardFromHandEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

public class AbstractAlly extends AbstractPermanent {
    private int _siteNumber;
    private int _strength;
    private int _vitality;
    private Race _race;

    public AbstractAlly(int twilight, int siteNumber, int strength, int vitality, Race race, Culture culture, String name) {
        this(twilight, siteNumber, strength, vitality, race, culture, name, false);
    }

    public AbstractAlly(int twilight, int siteNumber, int strength, int vitality, Race race, Culture culture, String name, boolean unique) {
        super(Side.FREE_PEOPLE, twilight, CardType.ALLY, culture, Zone.FREE_SUPPORT, name, unique);
        _siteNumber = siteNumber;
        _strength = strength;
        _vitality = vitality;
        _race = race;
    }

    public Race getRace() {
        return _race;
    }

    protected final List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canHealByDiscarding(game.getGameState(), game.getModifiersQuerying(), self)) {
            ActivateCardAction action = new ActivateCardAction(self, null);
            action.appendCost(new DiscardCardFromHandEffect(self));

            PhysicalCard active = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name(self.getBlueprint().getName()));
            if (active != null)
                action.appendEffect(new HealCharacterEffect(playerId, active));

            return Collections.singletonList(action);
        }
        return getExtraInPlayPhaseActions(playerId, game, self);
    }

    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        return null;
    }

    @Override
    public final int getSiteNumber() {
        return _siteNumber;
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
        return 0;
    }
}
