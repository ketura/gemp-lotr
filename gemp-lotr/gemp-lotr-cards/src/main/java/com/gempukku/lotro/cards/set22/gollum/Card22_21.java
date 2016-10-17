package com.gempukku.lotro.cards.set7.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;


import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: You may exert Gollum twice to play this event from your discard pile. Make a minion strength +2. If
 * that minion wins this skirmish, you may add a doubt.
 */
public class Card22_21 extends AbstractEvent {
    public Card22_21() {
        super(Side.SHADOW, 1, Culture.GOLLUM, "If He Loses", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.Minion) {
            @Override
            protected void cardSelected(LotroGame game, final PhysicalCard card) {
                action.appendEffect(new AddUntilEndOfPhaseModifierEffect(new StrengthModifier(self, card, 2)));
                action.appendEffect(
                        new AddUntilEndOfPhaseActionProxyEffect(new AbstractActionProxy() {
                    @Override
                    public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
                        if (TriggerConditions.winsSkirmish(game, effectResult, card)
                                && playerId.equals(card.getOwner())) {
                            OptionalTriggerAction action = new OptionalTriggerAction(self);
                            action.setVirtualCardAction(true);
                            action.appendEffect(
                                    new AddBurdenEffect(self.getOwner(), self, 1));
                            return Collections.singletonList(action);
                        }
                        return null;
                    }
                }));
            }
        });
        return action;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.isPhase(game, Phase.SKIRMISH)
                && PlayConditions.canExert(self, game, 2, Filters.name("Gollum"))
				&& PlayConditions.canPlayFromDiscard(playerId, game, self)) {
            final PlayEventAction playCardAction = getPlayCardAction(playerId, game, self, 0, false);
            return Collections.singletonList(playCardAction);
        }
        return null;
    }
}
