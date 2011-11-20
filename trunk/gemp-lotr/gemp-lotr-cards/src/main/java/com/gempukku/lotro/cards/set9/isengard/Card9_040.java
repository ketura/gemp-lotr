package com.gempukku.lotro.cards.set9.isengard;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Event • Response
 * Game Text: If an [ISENGARD] minion wins a skirmish, until the regroup phase, make that minion strength +1 for each
 * site you control and fierce.
 */
public class Card9_040 extends AbstractResponseEvent {
    public Card9_040() {
        super(Side.SHADOW, 1, Culture.ISENGARD, "Sack of the Shire");
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Culture.ISENGARD, CardType.MINION)
                && checkPlayRequirements(playerId, game, self, 0, false)) {
            final PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose ISENGARD minion", Culture.ISENGARD, CardType.MINION, Filters.inSkirmish) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new StrengthModifier(self, card, null, new CountSpottableEvaluator(Filters.siteControlled(playerId))), Phase.REGROUP));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, card, Keyword.FIERCE), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
