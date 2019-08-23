package com.gempukku.lotro.cards.set20.gandalf;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ReplaceFpCharacterInAssignmentEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * Protector of the West
 * Gandalf	Event • Skirmish
 * Spell.
 * Make Gandalf strength +2. If he wins this skirmish, have him replace an unbound companion assigned to another skirmish.
 */
public class Card20_168 extends AbstractEvent {
    public Card20_168() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "Protector of the West", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Filters.gandalf) {
                    @Override
                    protected void selectedCharacterCallback(final PhysicalCard gandalf) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                                                if (TriggerConditions.winsSkirmish(game, effectResult, gandalf)) {
                                                    final RequiredTriggerAction subAction = new RequiredTriggerAction(self);
                                                    subAction.appendEffect(
                                                            new ChooseActiveCardEffect(self, playerId, "Choose character to replace in skirmish with Gandalf", Filters.unboundCompanion, Filters.assignedToSkirmish, Filters.not(Filters.inSkirmish)) {
                                                                @Override
                                                                protected void cardSelected(LotroGame game, PhysicalCard card) {
                                                                    subAction.appendEffect(
                                                                            new ReplaceFpCharacterInAssignmentEffect(gandalf, card));
                                                                }
                                                            });
                                                    return Collections.singletonList(subAction);
                                                }
                                                return null;
                                            }
                                        }));
                    }
                });
        return action;
    }
}
