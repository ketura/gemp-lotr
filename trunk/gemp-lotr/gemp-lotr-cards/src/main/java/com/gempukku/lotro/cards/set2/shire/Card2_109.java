package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Spot Sting or Glamdring and exert its bearer X times to wound X Orcs or X Uruk-hai.
 */
public class Card2_109 extends AbstractEvent {
    public Card2_109() {
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Orc-bane", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.hasAttached(Filters.or(Filters.name("Sting"), Filters.name("Glamdring"))));
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseActiveCardEffect(self, playerId, "Choose Sting or Glamdring bearer", Filters.hasAttached(Filters.or(Filters.name("Sting"), Filters.name("Glamdring"))), Filters.canExert(self)) {
                    @Override
                    protected void cardSelected(final PhysicalCard bearer) {
                        int vitality = game.getModifiersQuerying().getVitality(game.getGameState(), bearer);
                        action.insertCost(
                                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                                        new IntegerAwaitingDecision(1, "Choose how many times to exert", 1, vitality - 1) {
                                            @Override
                                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                                final int exertionCount = getValidatedResult(result);
                                                for (int i = 0; i < exertionCount; i++) {
                                                    action.insertCost(
                                                            new ExertCharactersEffect(self, bearer));
                                                }
                                                action.appendEffect(
                                                        new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                                                                new MultipleChoiceAwaitingDecision(1, "Choose action to perform", new String[]{"Wound " + exertionCount + " Orcs", "Wound " + exertionCount + " Uruk-hai"}) {
                                                                    @Override
                                                                    protected void validDecisionMade(int index, String result) {
                                                                        Filter filter = (index == 0) ? Filters.race(Race.ORC) : Filters.race(Race.URUK_HAI);
                                                                        action.insertEffect(
                                                                                new ChooseAndWoundCharactersEffect(action, playerId, exertionCount, exertionCount, filter));
                                                                    }
                                                                }));
                                            }
                                        }
                                ));
                    }
                });

        return action;
    }
}
