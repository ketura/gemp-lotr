package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Exert 2 Elves to discard a condition.
 */
public class Card1_058 extends AbstractLotroCardBlueprint {
    public Card1_058() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.ELVEN, "The Seen and the Unseen");
        addKeyword(Keyword.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF), Filters.canExert()) >= 2) {
            final PlayEventAction action = new PlayEventAction(self);
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Exert first Elf", Filters.keyword(Keyword.ELF)) {
                        @Override
                        protected void cardSelected(final PhysicalCard firstElf) {
                            action.addCost(new ExertCharacterEffect(firstElf));
                            action.addCost(
                                    new ChooseActiveCardEffect(playerId, "Exert second Elf", Filters.keyword(Keyword.ELF), Filters.not(Filters.sameCard(firstElf))) {
                                        @Override
                                        protected void cardSelected(PhysicalCard secondElf) {
                                            action.addCost(new ExertCharacterEffect(secondElf));
                                        }
                                    });
                        }
                    });
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose condition", Filters.type(CardType.CONDITION)) {
                        @Override
                        protected void cardSelected(PhysicalCard condition) {
                            action.addEffect(new DiscardCardFromPlayEffect(condition));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
