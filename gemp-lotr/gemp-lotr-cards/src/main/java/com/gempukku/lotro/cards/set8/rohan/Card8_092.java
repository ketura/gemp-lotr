package com.gempukku.lotro.cards.set8.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Valiant. While you can spot a [ROHAN] Man, Theoden’s twilight cost is -1. When Theoden is killed, you may
 * play a [ROHAN] companion from your discard pile or draw deck.
 */
public class Card8_092 extends AbstractCompanion {
    public Card8_092() {
        super(3, 7, 3, 6, Culture.ROHAN, Race.MAN, Signet.ARAGORN, "Theoden", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (Filters.canSpot(gameState, modifiersQuerying, Culture.ROHAN, Race.MAN))
            return -1;
        return 0;
    }

    @Override
    public OptionalTriggerAction getKilledOptionalTrigger(String playerId, LotroGame game, PhysicalCard self) {
        if (playerId.equals(self.getOwner())) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndPlayCardFromDeckEffect(self.getOwner(), Culture.ROHAN, CardType.COMPANION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play from deck";
                        }
                    });
            possibleEffects.add(
                    new ChooseAndPlayCardFromDiscardEffect(self.getOwner(), game, Culture.ROHAN, CardType.COMPANION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Play from discard";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, self.getOwner(), possibleEffects));
            return action;
        }
        return null;
    }
}
