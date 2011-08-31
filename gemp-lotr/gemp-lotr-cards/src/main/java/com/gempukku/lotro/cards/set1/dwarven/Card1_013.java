package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion � Dwarf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Damage +1. Skirmish: Exert Gimli to make him strength +2.
 */
public class Card1_013 extends AbstractCompanion {
    public Card1_013() {
        super(2, 6, 3, Culture.DWARVEN, "Gimli", true);
        setSignet(Signet.GANDALF);
        addKeyword(Keyword.DWARF);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame lotroGame, PhysicalCard self) {
        LinkedList<Action> result = new LinkedList<Action>();

        appendPlayCompanionActions(result, lotroGame, self);
        appendHealCompanionActions(result, lotroGame, self);

        if (PlayConditions.canUseFPCardDuringPhase(lotroGame.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(lotroGame.getGameState(), lotroGame.getModifiersQuerying(), self)) {
            CostToEffectAction action = new CostToEffectAction(self, Keyword.SKIRMISH, "Exert Gimli to make him strength +2");

            action.addCost(new ExertCharacterEffect(self));
            action.addEffect(new AddUntilEndOfPhaseModifierEffect(new StrengthModifier(self, Filters.sameCard(self), 2), Phase.SKIRMISH));

            result.add(action);
        }

        return result;
    }
}
