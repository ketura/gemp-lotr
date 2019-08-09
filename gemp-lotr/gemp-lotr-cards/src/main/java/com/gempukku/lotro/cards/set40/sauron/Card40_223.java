package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.CancelEventEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.RoamingPenaltyModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.PlayEventResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Orc Ambusher
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Minion - Orc
 * Strength: 5
 * Vitality: 2
 * Home: 6
 * Card Number: 1C223
 * Game Text: Tracker. The roaming penalty for each [SAURON] minion is -1. Response: If a Free Peoples regroup event is played, exert this minion to cancel that event.
 */
public class Card40_223 extends AbstractMinion {
    public Card40_223() {
        super(1, 5, 2, 6, Race.ORC, Culture.SAURON, "Orc Ambusher");
        addKeyword(Keyword.TRACKER);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new RoamingPenaltyModifier(self, Filters.and(Filters.owner(self.getOwner()), Culture.SAURON, CardType.MINION), -1));
}

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Side.FREE_PEOPLE, CardType.EVENT, Keyword.REGROUP)
                && PlayConditions.canExert(self, game, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new CancelEventEffect(self, (PlayEventResult) effectResult));
            return Collections.singletonList(action);
        }
        return null;
    }
}
