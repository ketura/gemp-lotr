package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Dunlending Battle Cry
 * Dunland	Event • Skirmish
 * The twilight cost of this event is -1 for each site you control.
 * Make a [Dunland] Man strength +3. If that minion wins this skirmish, it is fierce until the regroup phase.
 */
public class Card20_029 extends AbstractEvent {
    public Card20_029() {
        super(Side.SHADOW, 2, Culture.DUNLAND, "Dunlending Battle Cry", Phase.SKIRMISH);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return -Filters.countActive(gameState, modifiersQuerying, Filters.siteControlled(self.getOwner()));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a DUNLAND Man", Culture.DUNLAND, Race.MAN) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard minion) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, minion, 3), Phase.SKIRMISH));
                        action.appendEffect(
                                new AddUntilEndOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                                               if (TriggerConditions.winsSkirmish(game, effectResult, minion)) {
                                                   RequiredTriggerAction action = new RequiredTriggerAction(self);
                                                   action.appendEffect(
                                                           new AddUntilStartOfPhaseModifierEffect(
                                                                   new KeywordModifier(self, minion, Keyword.FIERCE), Phase.REGROUP));
                                                   return Collections.singletonList(action);
                                               }
                                                return null;
                                            }
                                        }, Phase.SKIRMISH));
                    }
                });
        return action;
    }
}
