package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Legacy of Numenor
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition - Support Area
 * Card Number: 1U111
 * Game Text: Each time a [GONDOR] man wins a skirmish, heal that character.
 * Discard this condition if a [GONDOR] man loses a skirmish.
 */
public class Card40_111 extends AbstractPermanent {
    public Card40_111() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Legacy of Numenor", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Culture.GONDOR, Race.MAN)) {
            CharacterWonSkirmishResult result = (CharacterWonSkirmishResult) effectResult;
            final PhysicalCard winner = result.getWinner();
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, winner));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.losesSkirmish(game, effectResult, Culture.GONDOR, Race.MAN)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
