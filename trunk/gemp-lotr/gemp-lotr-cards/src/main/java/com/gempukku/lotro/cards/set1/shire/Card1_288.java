package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Ally • Home 1 • Hobbit
 * Strength: 2
 * Vitality: 3
 * Site: 1
 * Game Text: Fellowship: Exert Farmer Maggot to heal Merry or Pippin.
 */
public class Card1_288 extends AbstractAlly {
    public Card1_288() {
        super(1, 1, 2, 3, Culture.SHIRE, "Farmer Maggot", true);
        addKeyword(Keyword.HOBBIT);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            CostToEffectAction action = new CostToEffectAction(self, Keyword.FELLOWSHIP, "Exert Farmer Maggot to heal Merry or Pippin.");
            action.addCost(
                    new ExertCharacterEffect(self));

            PhysicalCard merry = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Merry"));
            PhysicalCard pippin = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Pippin"));

            List<Effect> possibleEffects = new LinkedList<Effect>();
            if (merry != null)
                possibleEffects.add(
                        new HealCharacterEffect(merry));
            if (pippin != null)
                possibleEffects.add(
                        new HealCharacterEffect(pippin));

            action.addEffect(
                    new ChoiceEffect(action, playerId, possibleEffects, false));

            return Collections.singletonList(action);
        }
        return null;
    }
}
