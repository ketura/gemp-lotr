package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion � Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10
 * Signet: Frodo
 * Game Text: Ring-bearer (resistance 10). Fellowship: Exert another companion who has the Frodo signet to heal Frodo.
 */
public class Card1_290 extends AbstractCompanion {
    public Card1_290() {
        super(0, 3, 4, Culture.SHIRE, "Frodo", true);
        setSignet(Signet.FRODO);
        addKeyword(Keyword.HOBBIT);
        addKeyword(Keyword.RING_BEARER);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        LinkedList<Action> actions = new LinkedList<Action>();

        appendPlayCompanionActions(actions, game, self);
        appendHealCompanionActions(actions, game, self);

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION), Filters.signet(Signet.FRODO), Filters.not(Filters.sameCard(self)), Filters.canExert())) {
            final CostToEffectAction action = new CostToEffectAction(self, Keyword.FELLOWSHIP, "Exert another companion who has the Frodo signet to heal Frodo.");
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose another companion who has the Frodo signet", Filters.type(CardType.COMPANION), Filters.signet(Signet.FRODO), Filters.not(Filters.sameCard(self)), Filters.canExert()) {
                        @Override
                        protected void cardSelected(PhysicalCard card) {
                            action.addCost(new ExertCharacterEffect(card));
                        }
                    });
            action.addEffect(
                    new HealCharacterEffect(self));

            actions.add(action);
        }

        return actions;
    }

    @Override
    public int getResistance() {
        return 10;
    }
}
