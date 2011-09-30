package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.effects.ChooseAndHealCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 10
 * Signet: Gandalf
 * Game Text: Ring-bearer (resistance 10). At the start of each of your turns, you may heal a Hobbit ally.
 */
public class Card1_289 extends AbstractCompanion {
    public Card1_289() {
        super(0, 3, 4, Culture.SHIRE, Race.HOBBIT, Signet.GANDALF, "Frodo", true);
        addKeyword(Keyword.RING_BEARER);
    }

    @Override
    public int getResistance() {
        return 10;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_TURN) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndHealCharacterEffect(action, playerId, Filters.type(CardType.ALLY), Filters.race(Race.HOBBIT)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
