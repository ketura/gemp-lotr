package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: Retribution
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event - Skirmish
 * Card Number: 1R26
 * Game Text: Make a Dwarf strength +1. If that Dwarf wins this skirmish, you may discard any combination of 2 Shadow conditions or possessions.
 */
public class Card40_026 extends AbstractEvent {
    public Card40_026() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Retribution", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a Dwarf", Race.DWARF) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard dwarf) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, dwarf, 1)));
                        action.appendEffect(
                                new AddUntilEndOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
                                                if (TriggerConditions.winsSkirmish(game, effectResult, dwarf)
                                                        && playerId.equals(dwarf.getOwner())) {
                                                    OptionalTriggerAction action = new OptionalTriggerAction(self);
                                                    action.setVirtualCardAction(true);
                                                    action.appendEffect(
                                                            new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 2, 2, Side.SHADOW, Filters.or(CardType.CONDITION, CardType.POSSESSION)));
                                                    return Collections.singletonList(action);
                                                }
                                                return null;
                                            }
                                        }));
                    }
                });
        return action;
    }
}
