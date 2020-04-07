package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 4
 * Type: Event
 * Game Text: Regroup: Exert a [RAIDER] Man to add a burden for each companion over 4. The Free Peoples player may
 * discard 2 companions (except the Ring-bearer) to prevent this.
 */
public class Card4_240 extends AbstractEvent {
    public Card4_240() {
        super(Side.SHADOW, 4, Culture.RAIDER, "New Fear", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.RAIDER, Race.MAN);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, final LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.RAIDER, Race.MAN));
        int burdens = Math.max(0, Filters.countActive(game, CardType.COMPANION) - 4);
        action.appendEffect(
                new PreventableEffect(action,
                        new AddBurdenEffect(self.getOwner(), self, burdens),
                        game.getGameState().getCurrentPlayerId(),
                        new PreventableEffect.PreventionCost() {
                            @Override
                            public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                return new ChooseAndDiscardCardsFromPlayEffect(subAction, playerId, 2, 2, CardType.COMPANION, Filters.not(Filters.ringBearer)) {
                                    @Override
                                    public String getText(LotroGame game) {
                                        return "Discard 2 companions (except the Ring-bearer)";
                                    }
                                };
                            }
                        }
                ));
        return action;
    }
}
