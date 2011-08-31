package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.PreventMinionBeingAssignedToCompanionModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Assignment: Exert this minion and spot a companion to prevent the opponent from assigning that
 * companion to this minion.
 */
public class Card1_147 extends AbstractMinion {
    public Card1_147() {
        super(4, 9, 2, 5, Culture.ISENGARD, "Uruk Guard");
        addKeyword(Keyword.URUK_HAI);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        appendPlayMinionAction(actions, game, self);

        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.ASSIGNMENT, self, 0)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            final CostToEffectAction action = new CostToEffectAction(self, Keyword.ASSIGNMENT, "Exert this minion and spot a companion to prevent the opponent from assigning that companion to this minion.");
            action.addCost(new ExertCharacterEffect(self));
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose a companion", Filters.type(CardType.COMPANION)) {
                        @Override
                        protected void cardSelected(PhysicalCard companion) {
                            action.addEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new PreventMinionBeingAssignedToCompanionModifier(self, Filters.sameCard(companion), Filters.sameCard(self))
                                            , Phase.ASSIGNMENT));
                        }
                    });
            actions.add(action);
        }

        return actions;
    }
}
