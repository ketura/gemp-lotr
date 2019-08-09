package com.gempukku.lotro.cards.set4.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.logic.effects.PreventAllWoundsActionProxy;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Spell.
 * Skirmish: Spot Gandalf to prevent all wounds to him. Any Shadow player may make you wound a minion to prevent this.
 */
public class Card4_097 extends AbstractEvent {
    public Card4_097() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "Long I Fell", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && Filters.canSpot(game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PreventableEffect(action,
                        new AddUntilEndOfPhaseActionProxyEffect(
                                new PreventAllWoundsActionProxy(self, Filters.gandalf)) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Prevent all wounds to Gandalf";
                            }
                        },
                        GameUtils.getShadowPlayers(game),
                        new PreventableEffect.PreventionCost() {
                            @Override
                            public Effect createPreventionCostForPlayer(SubAction subAction, String shadowPlayerId) {
                                return new ChooseAndWoundCharactersEffect(subAction, playerId, 1, 1, CardType.MINION) {
                                    @Override
                                    public String getText(LotroGame game) {
                                        return "Make fellowship player wound a minion";
                                    }
                                };
                            }
                        }
                ));
        return action;
    }
}
