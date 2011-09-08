package com.gempukku.lotro.cards;

import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardCardFromHandEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

public abstract class AbstractCompanion extends AbstractLotroCardBlueprint {
    private int _twilightCost;
    private int _strength;
    private int _vitality;
    private Keyword _race;
    private Signet _signet;

    public AbstractCompanion(int twilightCost, int strength, int vitality, Culture culture, Keyword race, Signet signet, String name) {
        this(twilightCost, strength, vitality, culture, race, signet, name, false);
    }

    public AbstractCompanion(int twilightCost, int strength, int vitality, Culture culture, Keyword race, Signet signet, String name, boolean unique) {
        super(Side.FREE_PEOPLE, CardType.COMPANION, culture, name, unique);
        _twilightCost = twilightCost;
        _strength = strength;
        _vitality = vitality;
        _race = race;
        _signet = signet;
        if (race != null)
            addKeyword(race);
    }

    public Keyword getRace() {
        return _race;
    }

    @Override
    public Signet getSignet() {
        return _signet;
    }

    private void appendPlayCompanionActions(List<Action> actions, String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, Phase.FELLOWSHIP, self))
            actions.add(getPlayCardAction(playerId, game, self, 0));
    }

    private void appendHealCompanionActions(List<Action> actions, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canHealByDiscarding(game.getGameState(), game.getModifiersQuerying(), self)) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Discard card to heal");
            action.addCost(new DiscardCardFromHandEffect(self));

            PhysicalCard active = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name(self.getBlueprint().getName()));
            if (active != null)
                action.addEffect(new HealCharacterEffect(active));

            actions.add(action);
        }
    }

    @Override
    public final List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        if (checkPlayRequirements(playerId, game, self, 0))
            appendPlayCompanionActions(actions, playerId, game, self);

        appendHealCompanionActions(actions, game, self);

        List<? extends Action> extraActions = getExtraPhaseActions(playerId, game, self);
        if (extraActions != null)
            actions.addAll(extraActions);

        return actions;
    }

    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.checkUniqueness(game.getGameState(), game.getModifiersQuerying(), self)
                && PlayConditions.checkRuleOfNine(game.getGameState(), game.getModifiersQuerying(), self);
    }

    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.FREE_CHARACTERS);
    }

    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        return null;
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
