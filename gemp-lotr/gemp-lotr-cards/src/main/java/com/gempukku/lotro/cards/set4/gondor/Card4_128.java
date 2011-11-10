package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Spot a Ring-bound Man to make a minion's site number +2. If that minion loses this skirmish,
 * you may wound a minion.
 */
public class Card4_128 extends AbstractOldEvent {
    public Card4_128() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "New Errand", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.MAN), Filters.keyword(Keyword.RING_BOUND));
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a minion", Filters.type(CardType.MINION)) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard card) {
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new MinionSiteNumberModifier(self, Filters.sameCard(card), null, 2), Phase.SKIRMISH));
                        action.appendEffect(
                                new AddUntilEndOfPhaseActionProxyEffect(
                                        new AbstractActionProxy() {
                                            @Override
                                            public List<? extends Action> getRequiredAfterTriggers(LotroGame lotroGame, EffectResult effectResults) {
                                                if (PlayConditions.losesSkirmish(lotroGame.getGameState(), lotroGame.getModifiersQuerying(), effectResults, Filters.sameCard(card))) {
                                                    final RequiredTriggerAction action = new RequiredTriggerAction(self);
                                                    action.appendEffect(
                                                            new PlayoutDecisionEffect(lotroGame.getUserFeedback(), playerId,
                                                                    new MultipleChoiceAwaitingDecision(1, "Would you like to wound " + card.getBlueprint().getName(), new String[]{"Yes", "No"}) {
                                                                        @Override
                                                                        protected void validDecisionMade(int index, String result) {
                                                                            if (result.equals("Yes")) {
                                                                                action.insertEffect(
                                                                                        new WoundCharactersEffect(self, card));
                                                                            }
                                                                        }
                                                                    }));
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
