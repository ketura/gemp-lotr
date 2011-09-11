package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 4
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 4
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Ranger. At the start of each of your turns, you may heal another companion who has the Aragorn signet.
 */
public class Card1_365 extends AbstractCompanion {
    public Card1_365() {
        super(4, 8, 4, Culture.GONDOR, Race.MAN, Signet.ARAGORN, "Aragorn", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_TURN) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self, null, "Heal another companion who has the Aragorn signet");
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose companion to heal", Filters.type(CardType.COMPANION), Filters.signet(Signet.ARAGORN), Filters.not(Filters.sameCard(self))) {
                        @Override
                        protected void cardSelected(PhysicalCard companion) {
                            action.addEffect(new HealCharacterEffect(companion));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
