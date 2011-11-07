package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.PutRandomCardFromHandOnBottomOfDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Effect;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a [ROHAN] Man strength +4. Any Shadow player may place a random card from hand beneath his or her
 * draw deck to make that Man strength +2 instead.
 */
public class Card7_243 extends AbstractEvent {
    public Card7_243() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Morning Came", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose ROHAN Man", Culture.ROHAN, Race.MAN) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard card) {
                        action.appendEffect(
                                new PreventableEffect(action,
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new StrengthModifier(self, card, 4), Phase.SKIRMISH) {
                                            @Override
                                            public String getText(LotroGame game) {
                                                return "Make " + GameUtils.getCardLink(card) + " strength +4";
                                            }
                                        }, GameUtils.getOpponents(game, playerId),
                                        new PreventableEffect.PreventionCost() {
                                            @Override
                                            public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                                return new PutRandomCardFromHandOnBottomOfDeckEffect(playerId) {
                                                    @Override
                                                    public String getText(LotroGame game) {
                                                        return "Place a random card from hand beneath your draw deck to make that Man strength +2 instead";
                                                    }

                                                    @Override
                                                    protected void putCardFromHandOnBottomOfDeckCallback(PhysicalCard card) {
                                                        action.appendEffect(
                                                                new AddUntilEndOfPhaseModifierEffect(
                                                                        new StrengthModifier(self, card, 2), Phase.SKIRMISH));
                                                    }
                                                };
                                            }
                                        }
                                ));
                    }
                });
        return action;
    }
}
