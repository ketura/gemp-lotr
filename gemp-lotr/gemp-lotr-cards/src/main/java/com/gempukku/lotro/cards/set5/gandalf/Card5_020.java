package com.gempukku.lotro.cards.set5.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Spell. Maneuver: Exert Gandalf to discard up to 2 shadow possessions. Any Shadow player may discard
 * a minion to prevent this.
 */
public class Card5_020 extends AbstractEvent {
    public Card5_020() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Turn of the Tide", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.gandalf));
        action.appendEffect(
                new PreventableEffect(action,
                        new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 0, 2, Side.SHADOW, CardType.POSSESSION) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Discard up to 2 shadow possessions";
                            }
                        }, GameUtils.getShadowPlayers(game),
                        new PreventableEffect.PreventionCost() {
                            @Override
                            public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                return new ChooseAndDiscardCardsFromPlayEffect(subAction, playerId, 1, 1, CardType.MINION) {
                                    @Override
                                    public String getText(LotroGame game) {
                                        return "Discard a minion";
                                    }
                                };
                            }
                        }
                ));
        return action;
    }
}
