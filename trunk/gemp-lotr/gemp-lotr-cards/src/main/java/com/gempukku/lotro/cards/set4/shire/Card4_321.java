package com.gempukku.lotro.cards.set4.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CancelSkirmishEffect;
import com.gempukku.lotro.cards.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Stealth. Skirmish: At sites 1T to 5T, cancel a skirmish involving a Hobbit. At any other site, prevent
 * a Hobbit from taking more than 1 wound.
 */
public class Card4_321 extends AbstractEvent {
    public Card4_321() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "Swiftly and Softly", Phase.SKIRMISH);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        boolean firstOption = (game.getGameState().getCurrentSiteNumber() <= 5 && game.getGameState().getCurrentSiteBlock() == Block.TWO_TOWERS);
        if (firstOption) {
            action.appendEffect(
                    new CancelSkirmishEffect(Race.HOBBIT));
        } else {
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a Hobbit", Race.HOBBIT) {
                        @Override
                        protected void cardSelected(LotroGame game, final PhysicalCard hobbit) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseActionProxyEffect(
                                            new AbstractActionProxy() {
                                                @Override
                                                public List<? extends Action> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResults) {
                                                    if (PlayConditions.isWounded(effectResults, hobbit)) {
                                                        RequiredTriggerAction action = new RequiredTriggerAction(self);
                                                        action.appendEffect(
                                                                new AddUntilEndOfPhaseModifierEffect(
                                                                        new CantTakeWoundsModifier(self, Filters.sameCard(hobbit)), Phase.SKIRMISH));
                                                        return Collections.singletonList(action);
                                                    }
                                                    return null;
                                                }
                                            }, Phase.SKIRMISH));
                        }
                    });
        }
        return action;
    }
}
