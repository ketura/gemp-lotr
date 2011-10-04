package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.EndOfPhaseResult;
import com.gempukku.lotro.logic.timing.results.StartOfPhaseResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Search. Plays to your support area. At the beginning of each of your Shadow phases, draw 1 card. At the
 * end of each of your Shadow phases, exert a Nazgul or discard this condition.
 */
public class Card1_206 extends AbstractPermanent {
    public Card1_206() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "Bent on Discovery");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                && ((StartOfPhaseResult) effectResult).getPhase() == Phase.SHADOW) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DrawCardEffect(self.getOwner(), 1));
            return Collections.singletonList(action);
        }
        if (effectResult.getType() == EffectResult.Type.END_OF_PHASE
                && ((EndOfPhaseResult) effectResult).getPhase() == Phase.SHADOW) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, Filters.race(Race.NAZGUL)));
            possibleEffects.add(
                    new DiscardCardsFromPlayEffect(self, self));

            action.appendEffect(
                    new ChoiceEffect(action, self.getOwner(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
