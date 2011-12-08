package com.gempukku.lotro.cards.set12.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: When you play this companion during the fellowship phase, choose a Shadow player who must discard the
 * top 2 cards of his or her draw deck.
 */
public class Card12_007 extends AbstractCompanion {
    public Card12_007() {
        super(2, 6, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Dwarven Warrior");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.isPhase(game, Phase.FELLOWSHIP)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ChooseOpponentEffect(game.getGameState().getCurrentPlayerId()) {
                        @Override
                        protected void opponentChosen(String opponentId) {
                            action.appendEffect(
                                    new DiscardTopCardFromDeckEffect(self, opponentId, 2, true));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
