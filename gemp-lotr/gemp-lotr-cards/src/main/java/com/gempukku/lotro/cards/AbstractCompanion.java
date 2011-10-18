package com.gempukku.lotro.cards;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

public abstract class AbstractCompanion extends AbstractPermanent {
    private int _strength;
    private int _vitality;
    private Race _race;
    private Signet _signet;

    public AbstractCompanion(int twilightCost, int strength, int vitality, Culture culture, Race race, Signet signet, String name) {
        this(twilightCost, strength, vitality, culture, race, signet, name, false);
    }

    public AbstractCompanion(int twilightCost, int strength, int vitality, Culture culture, Race race, Signet signet, String name, boolean unique) {
        super(Side.FREE_PEOPLE, twilightCost, CardType.COMPANION, culture, Zone.FREE_CHARACTERS, name, unique);
        _strength = strength;
        _vitality = vitality;
        _race = race;
        _signet = signet;
    }

    public Race getRace() {
        return _race;
    }

    @Override
    public Signet getSignet() {
        return _signet;
    }

    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.checkRuleOfNine(game.getGameState(), game.getModifiersQuerying(), self);
    }

    protected final List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canHealByDiscarding(game.getGameState(), game.getModifiersQuerying(), self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new DiscardCardsFromHandEffect(null, playerId, Collections.singleton(self), false));

            PhysicalCard active = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name(self.getBlueprint().getName()));
            if (active != null)
                action.appendEffect(new HealCharactersEffect(playerId, active));

            return Collections.singletonList(action);
        }

        return getExtraInPlayPhaseActions(playerId, game, self);
    }

    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        return null;
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
