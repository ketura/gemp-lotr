package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each Dwarf is damage +1.
 */
public class Card1_021 extends AbstractLotroCardBlueprint {
    public Card1_021() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, Culture.DWARVEN, "Lord of Moria", "1_21");
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (game.getGameState().getCurrentPhase() == Phase.FELLOWSHIP
                && self.getZone() == Zone.HAND) {
            PlayPermanentAction action = new PlayPermanentAction(self, Zone.FREE_SUPPORT);
            return Collections.<Action>singletonList(action);
        }
        return null;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new KeywordModifier(self, Filters.keyword(Keyword.DWARF), Keyword.DAMAGE);
    }
}
