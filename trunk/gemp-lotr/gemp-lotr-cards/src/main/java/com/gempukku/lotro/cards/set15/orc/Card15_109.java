package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 6
 * Type: Minion • Orc
 * Strength: 13
 * Vitality: 3
 * Site: 4
 * Game Text: Hunter 3. (While skirmishing a non-hunter character, this character is strength +3.) When you play Gorbag,
 * you may discard a card from the top of the Free Peoples player’s deck for each hunter character you can spot.
 */
public class Card15_109 extends AbstractMinion {
    public Card15_109() {
        super(6, 13, 3, 4, Race.ORC, Culture.ORC, "Gorbag", true);
        addKeyword(Keyword.HUNTER, 3);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DiscardTopCardFromDeckEffect(self, game.getGameState().getCurrentPlayerId(),
                            Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.character, Keyword.HUNTER), true));
            return Collections.singletonList(action);
        }
        return null;
    }
}
