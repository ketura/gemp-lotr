package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
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
 * Twilight Cost: 1
 * Type: Ally � Home 3 � Dwarf
 * Strength: 3
 * Vitality: 3
 * Site: 3
 * Game Text: Fellowship: Exert Grimir to shuffle a [DWARVEN] event from your discard pile into your draw deck.
 */
public class Card1_017 extends AbstractAlly {
    public Card1_017() {
        super(1, 3, 3, 3, Culture.DWARVEN, "Grimir", "1_17", true);
        addKeyword(Keyword.DWARF);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        appendPlayAllyActions(actions, game, self);
        appendHealAllyActions(actions, game, self);

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            CostToEffectAction action = new CostToEffectAction(self, "Exert Grimir to shuffle a DWARVEN event from your discard pile into draw deck");
            action.addCost(new ExertCharacterEffect(self));
            action.addEffect(
                    new ChooseAnyCardEffect(playerId, "Choose card to shuffle into draw deck", Filters.zone(Zone.DISCARD), Filters.owner(playerId), Filters.type(CardType.EVENT), Filters.culture(Culture.DWARVEN)) {
                        @Override
                        protected void cardSelected(PhysicalCard card) {
                            game.getGameState().putCardOnBottomOfDeck(card);
                            game.getGameState().shuffleDeck(playerId);
                        }
                    }
            );
            actions.add(action);
        }

        return actions;
    }
}
