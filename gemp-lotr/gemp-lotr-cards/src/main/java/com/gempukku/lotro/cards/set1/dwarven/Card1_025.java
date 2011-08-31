package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.HealCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a Dwarf wins a skirmish, heal that Dwarf.
 */
public class Card1_025 extends AbstractLotroCardBlueprint {
    public Card1_025() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.DWARVEN, "Still Draws Breath");
        addKeyword(Keyword.RESPONSE);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getPlayableWhenActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (self.getZone() == Zone.HAND && PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.keyword(Keyword.DWARF))) {
            SkirmishResult skirmishResult = (SkirmishResult) effectResult;
            PlayEventAction action = new PlayEventAction(self);
            action.addEffect(new HealCharacterEffect(skirmishResult.getWinners().get(0)));
            return Collections.<Action>singletonList(action);
        }
        return null;
    }
}
