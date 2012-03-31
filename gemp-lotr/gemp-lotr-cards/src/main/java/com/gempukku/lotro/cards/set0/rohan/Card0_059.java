package com.gempukku.lotro.cards.set0.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Promotional
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 5
 * Game Text: If you can spot a [ROHAN] Man, you may play Erkenbrand any time you could play a skirmish event. When you
 * play Erkenbrand, you may discard a Shadow possession.
 */
public class Card0_059 extends AbstractCompanion {
    public Card0_059() {
        super(3, 7, 3, 5, Culture.ROHAN, Race.MAN, null, "Erkenbrand", "Master of Westfold", true);
    }

    @Override
    protected Phase getExtraPlayableInPhase(LotroGame game) {
        if (PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN))
            return Phase.SKIRMISH;
        return null;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.SHADOW, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
