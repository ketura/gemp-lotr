package com.gempukku.lotro.cards.set5.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.conditions.PhaseCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Game Text: Knight. The twilight cost of each other knight in your starting fellowship is -1.
 * Assignment: Assign Alcarin to a minion bearing a [GONDOR] fortification to heal Alcarin.
 */
public class Card5_031 extends AbstractCompanion {
    public Card5_031() {
        super(3, 7, 3, Culture.GONDOR, Race.MAN, null, "Alcarin", true);
        addKeyword(Keyword.KNIGHT);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new TwilightCostModifier(self,
                        Filters.and(
                                Filters.not(Filters.sameCard(self)),
                                Keyword.KNIGHT
                        ), new PhaseCondition(Phase.GAME_SETUP), -1));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.ASSIGNMENT, self)
                && PlayConditions.canCardAssignToSkirmish(self, game, self)
                && PlayConditions.canCardAssignToSkirmish(self, game,
                Filters.and(CardType.MINION, Filters.hasAttached(Culture.GONDOR, Keyword.FORTIFICATION)))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose minion", CardType.MINION, Filters.hasAttached(Culture.GONDOR, Keyword.FORTIFICATION)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertCost(
                                    new AssignmentEffect(playerId, self, card));
                        }
                    });
            action.appendEffect(
                    new HealCharactersEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
