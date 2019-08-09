package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Legacy of Numenor
 * Gondor	Condition • Support Area
 * Each time a [Gondor] ranger wins a skirmish involving a roaming minion, heal that ranger.
 * Discard this condition if a [Gondor] ranger loses a skirmish.
 */
public class Card20_198 extends AbstractPermanent {
    public Card20_198() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, "Legacy of Numenor", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmishInvolving(game, effectResult, Filters.and(Culture.GONDOR, Keyword.RANGER), Filters.and(CardType.MINION, Keyword.ROAMING))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new HealCharactersEffect(self, ((CharacterWonSkirmishResult) effectResult).getWinner()));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.losesSkirmish(game, effectResult, Culture.GONDOR, Keyword.RANGER)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
