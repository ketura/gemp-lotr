package com.gempukku.lotro.cards.set9.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Wizard
 * Strength: 8
 * Vitality: 4
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: The move limit is +1. Each time the fellowship moves during the regroup phase, each opponent may discard
 * 2 cards to draw 2 cards.
 */
public class Card9_026 extends AbstractCompanion {
    public Card9_026() {
        super(4, 8, 4, 6, Culture.GANDALF, Race.WIZARD, Signet.GANDALF, "Radagast", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new MoveLimitModifier(self, 1));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_FELLOWSHIP_MOVES
                && PlayConditions.isPhase(game, Phase.REGROUP)) {
            List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();
            for (final String opponent : GameUtils.getOpponents(game, self.getOwner())) {
                if (PlayConditions.canDiscardFromHand(game, opponent, 2, Filters.any)) {
                    final RequiredTriggerAction action = new RequiredTriggerAction(self);
                    action.appendCost(
                            new OptionalEffect(action, opponent,
                                    new ChooseAndDiscardCardsFromHandEffect(action, opponent, false, 2) {
                                        @Override
                                        public String getText(LotroGame game) {
                                            return "Discard 2 cards to draw 2 cards";
                                        }

                                        @Override
                                        protected void cardsBeingDiscardedCallback(Collection<PhysicalCard> cardsBeingDiscarded) {
                                            if (cardsBeingDiscarded.size() == 2)
                                                action.appendEffect(
                                                        new DrawCardsEffect(opponent, 2));
                                        }
                                    }));
                    actions.add(action);
                }
            }
            return actions;
        }
        return null;
    }
}
