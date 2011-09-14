package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
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
 * Vitality: 2
 * Site: 1
 * Game Text: Fellowship: Exert The Gaffer to heal Frodo or Sam.
 */
public class Card1_291 extends AbstractAlly {
    public Card1_291() {
        super(1, 1, 2, 2, Race.HOBBIT, Culture.SHIRE, "The Gaffer", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Exert The Gaffer to heal Frodo or Sam.");
            action.addCost(
                    new ExertCharacterEffect(playerId, self));

            PhysicalCard frodo = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Frodo"));
            PhysicalCard sam = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Sam"));

            List<Effect> possibleEffects = new LinkedList<Effect>();
            if (frodo != null)
                possibleEffects.add(
                        new HealCharacterEffect(playerId, frodo));
            if (sam != null)
                possibleEffects.add(
                        new HealCharacterEffect(playerId, sam));

            action.addEffect(
                    new ChoiceEffect(action, playerId, possibleEffects, false));

            return Collections.singletonList(action);
        }
        return null;
    }
}
