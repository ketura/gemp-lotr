package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.SpotEffect;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Archery: Spot an Elf companion to make the minion archery total -1.
 */
public class Card1_052 extends AbstractLotroCardBlueprint {
    public Card1_052() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.ELVEN, "Lightfootedness", "1_52");
        addKeyword(Keyword.ARCHERY);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.ARCHERY, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF), Filters.type(CardType.COMPANION))) {
            PlayEventAction action = new PlayEventAction(self);
            action.addCost(new SpotEffect(Filters.and(Filters.keyword(Keyword.ELF), Filters.type(CardType.COMPANION))));
            action.addEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ArcheryTotalModifier(self, Side.SHADOW, -1), Phase.ARCHERY));

            return Collections.singletonList(action);
        }
        return null;
    }
}
