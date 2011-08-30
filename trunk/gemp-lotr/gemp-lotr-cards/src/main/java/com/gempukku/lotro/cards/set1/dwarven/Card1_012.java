package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTwilightEffect;
import com.gempukku.lotro.cards.effects.PutCardFromHandOnBottomOfDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseAnyCardEffect;
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
 * Signet: Aragorn
 * Game Text: Damage +1. Fellowship: If the twilight pool has fewer than 2 twilight tokens, add (2) to place a card from
 * hand beneath your draw deck.
 */
public class Card1_012 extends AbstractCompanion {
    public Card1_012() {
        super(2, 6, 3, Culture.DWARVEN, "Gimli", "1_12", true);
        setSignet(Signet.ARAGORN);
        addKeyword(Keyword.DWARF);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, final LotroGame lotroGame, PhysicalCard self) {
        LinkedList<Action> result = new LinkedList<Action>();

        appendPlayCompanionActions(result, lotroGame, self);
        appendHealCompanionActions(result, lotroGame, self);

        if (PlayConditions.canUseFPCardDuringPhase(lotroGame.getGameState(), Phase.FELLOWSHIP, self)
                && lotroGame.getGameState().getTwilightPool() < 2) {
            final CostToEffectAction costToEffectAction = new CostToEffectAction(self, "Add (2) to place a card from hand beneath your draw deck");

            costToEffectAction.addCost(new AddTwilightEffect(2));
            costToEffectAction.addEffect(
                    new ChooseAnyCardEffect(playerId, "Choose a card to place beneath your draw deck", Filters.zone(Zone.HAND), Filters.owner(playerId)) {
                        @Override
                        protected void cardSelected(PhysicalCard card) {
                            costToEffectAction.addEffect(new PutCardFromHandOnBottomOfDeckEffect(card));
                        }
                    });

            result.add(costToEffectAction);
        }

        return result;
    }
}
