package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RemoveBurderEffect;
import com.gempukku.lotro.cards.effects.SpotEffect;
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
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Spot an Elf to remove a burden.
 */
public class Card1_039 extends AbstractLotroCardBlueprint {
    public Card1_039() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.ELVEN, "Elf-song", "1_39");
        addKeyword(Keyword.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF))) {
            PlayEventAction action = new PlayEventAction(self);
            action.addCost(new SpotEffect(Filters.keyword(Keyword.ELF)));
            action.addEffect(new RemoveBurderEffect(playerId));

            return Collections.singletonList(action);
        }
        return null;
    }
}
