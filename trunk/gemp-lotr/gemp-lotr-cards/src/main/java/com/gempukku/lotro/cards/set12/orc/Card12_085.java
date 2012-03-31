package com.gempukku.lotro.cards.set12.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 12
 * Type: Minion • Troll
 * Strength: 15
 * Vitality: 4
 * Site: 4
 * Game Text: Damage +1. Fierce. Toil 2. (For each [ORC] character you exert when playing this, its twilight cost is -2)
 * Each time an [ORC] lurker wins a skirmish, you may make the Free Peoples player discard the top 2 cards of his
 * or her draw deck.
 */
public class Card12_085 extends AbstractMinion {
    public Card12_085() {
        super(12, 15, 4, 4, Race.TROLL, Culture.ORC, "Cave Troll of Moria", "Savage Menace", true);
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.FIERCE);
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Culture.ORC, Keyword.LURKER)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new DiscardTopCardFromDeckEffect(self, game.getGameState().getCurrentPlayerId(), 2, true));
            return Collections.singletonList(action);
        }
        return null;
    }
}
