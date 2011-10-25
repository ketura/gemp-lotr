package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 5
 * Type: Minion • Man
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: Easterling. Fierce.
 * Skirmish: Spot 2 burdens and remove (2) to make an Easterling strength +2.
 * Skirmish: Spot 4 burdens and remove (2) to make an Easterling strength +3.
 * Skirmish: Spot 6 burdens and remove (2) to make an Easterling strength +4.
 */
public class Card4_225 extends AbstractMinion {
    public Card4_225() {
        super(5, 11, 3, 4, Race.MAN, Culture.RAIDER, "Easterling Captain", true);
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 2)) {
            List<Action> actions = new LinkedList<Action>();

            if (game.getGameState().getBurdens() >= 2) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Make Easterling strength +2");
                action.appendCost(
                        new RemoveTwilightEffect(2));
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose an Easterling", Filters.keyword(Keyword.EASTERLING)) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                action.insertEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new StrengthModifier(self, Filters.sameCard(card), 2), Phase.SKIRMISH));
                            }
                        });
                actions.add(action);
            }
            if (game.getGameState().getBurdens() >= 4) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Make Easterling strength +3");
                action.appendCost(
                        new RemoveTwilightEffect(2));
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose an Easterling", Filters.keyword(Keyword.EASTERLING)) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                action.insertEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new StrengthModifier(self, Filters.sameCard(card), 3), Phase.SKIRMISH));
                            }
                        });
                actions.add(action);
            }
            if (game.getGameState().getBurdens() >= 6) {
                final ActivateCardAction action = new ActivateCardAction(self);
                action.setText("Make Easterling strength +4");
                action.appendCost(
                        new RemoveTwilightEffect(2));
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose an Easterling", Filters.keyword(Keyword.EASTERLING)) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                action.insertEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new StrengthModifier(self, Filters.sameCard(card), 4), Phase.SKIRMISH));
                            }
                        });
                actions.add(action);
            }

            return actions;
        }
        return null;
    }
}
