package com.gempukku.lotro.cards.set7.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.actions.SubCostToEffectAction;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a Dwarf strength +2. You may also exert that Dwarf to draw 2 cards.
 */
public class Card7_011 extends AbstractEvent {
    public Card7_011() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Out of Darkness", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a Dwarf", Race.DWARF) {
                    @Override
                    protected void cardSelected(final LotroGame game, final PhysicalCard card) {
                        game.getModifiersEnvironment().addUntilEndOfPhaseModifier(
                                new StrengthModifier(self, card, 2), Phase.SKIRMISH);
                        if (PlayConditions.canExert(self, game, card)) {
                            action.appendEffect(
                                    new PlayoutDecisionEffect(playerId,
                                            new MultipleChoiceAwaitingDecision(1, "Do you want to exert that Dwarf to draw 2 cards?", new String[]{"Yes", "No"}) {
                                                @Override
                                                protected void validDecisionMade(int index, String result) {
                                                    if (index == 0) {
                                                        SubCostToEffectAction subAction = new SubCostToEffectAction(action);
                                                        subAction.appendCost(
                                                                new ExertCharactersEffect(self, card));
                                                        subAction.appendEffect(
                                                                new DrawCardEffect(playerId, 2));
                                                        game.getActionsEnvironment().addActionToStack(subAction);
                                                    }
                                                }
                                            }));
                        }
                    }
                });
        return action;
    }
}
