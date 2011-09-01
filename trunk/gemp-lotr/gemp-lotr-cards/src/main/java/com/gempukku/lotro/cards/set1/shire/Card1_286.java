package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.OverwhelmedByMultiplierModifier;
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
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Ally • Home 2 • Hobbit
 * Strength: 2
 * Vitality: 2
 * Site: 2
 * Game Text: Skirmish: Exert this ally to prevent a Hobbit from being overwhelmed unless that Hobbit's strength is
 * tripled.
 */
public class Card1_286 extends AbstractAlly {
    public Card1_286() {
        super(1, 2, 2, 2, Culture.SHIRE, "Bounder");
        addKeyword(Keyword.HOBBIT);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();
        appendPlayAllyActions(actions, game, self);
        appendHealAllyActions(actions, game, self);

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            final CostToEffectAction action = new CostToEffectAction(self, Keyword.SKIRMISH, "Exert this ally to prevent a Hobbit from being overwhelmed unless that Hobbit's strength is tripled.");
            action.addCost(new ExertCharacterEffect(self));
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose a Hobbit", Filters.keyword(Keyword.HOBBIT)) {
                        @Override
                        protected void cardSelected(PhysicalCard hobbit) {
                            action.addEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new OverwhelmedByMultiplierModifier(self, Filters.sameCard(hobbit), 3), Phase.SKIRMISH));
                        }
                    }
            );
            actions.add(action);
        }
        return actions;
    }
}
