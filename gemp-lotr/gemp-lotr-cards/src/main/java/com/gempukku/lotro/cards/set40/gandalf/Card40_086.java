package com.gempukku.lotro.cards.set40.gandalf;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ReplaceInSkirmishEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: Protector of the West
 * Set: Second Edition
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event - Skirmish
 * Card Number: 1U86
 * Game Text: Make Gandalf strength +2. If he wins this skirmish, you may have him replace another unbound companion assigned to a skirmish.
 */
public class Card40_086 extends AbstractEvent {
    public Card40_086() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "Protector of the West", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Filters.gandalf) {
                    @Override
                    protected void selectedCharacterCallback(final PhysicalCard gandalf) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult) {
                                                if (playerId.equals(self.getOwner())
                                                        && TriggerConditions.winsSkirmish(game, effectResult, gandalf)) {
                                                    final OptionalTriggerAction action = new OptionalTriggerAction(self);
                                                    action.setVirtualCardAction(true);
                                                    action.appendEffect(
                                                            new ChooseActiveCardEffect(self, playerId, "Choose unbound companion assigned to skirmish",
                                                                    Filters.unboundCompanion, Filters.not(gandalf), Filters.assignedToSkirmish) {
                                                                @Override
                                                                protected void cardSelected(LotroGame game, PhysicalCard card) {
                                                                    action.appendEffect(
                                                                            new ReplaceInSkirmishEffect(gandalf, card));
                                                                }
                                                            });
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
