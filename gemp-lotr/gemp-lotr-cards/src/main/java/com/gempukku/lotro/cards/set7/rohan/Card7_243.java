package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.PutRandomCardFromHandOnBottomOfDeckEffect;
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
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose ROHAN Man", Culture.ROHAN, Race.MAN) {
                    @Override
                    protected void cardSelected(LotroGame game, final PhysicalCard rohanMan) {
                        action.appendEffect(
                                new PreventableEffect(action,
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new StrengthModifier(self, rohanMan, 4)) {
                                            @Override
                                            public String getText(LotroGame game) {
                                                return "Make " + GameUtils.getFullName(rohanMan) + " strength +4";
                                            }
                                        }, GameUtils.getShadowPlayers(game),
                                        new PreventableEffect.PreventionCost() {
                                            @Override
                                            public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                                return new PutRandomCardFromHandOnBottomOfDeckEffect(playerId) {
                                                    @Override
                                                    public String getText(LotroGame game) {
                                                        return "Place a random card from hand beneath your draw deck to make that Man strength +2 instead";
                                                    }

                                                    @Override
                                                    protected void putCardFromHandOnBottomOfDeckCallback(PhysicalCard card) {
                                                        action.appendEffect(
                                                                new AddUntilEndOfPhaseModifierEffect(
                                                                        new StrengthModifier(self, rohanMan, 2)));
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
