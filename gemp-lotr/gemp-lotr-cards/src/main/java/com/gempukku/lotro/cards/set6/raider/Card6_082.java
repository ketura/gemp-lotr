package com.gempukku.lotro.cards.set6.raider;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.Effect;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Maneuver: Spot a mounted [RAIDER] Man to discard a companion (except the Ring-bearer). The Free Peoples
 * player may add (4) to prevent this.
 */
public class Card6_082 extends AbstractEvent {
    public Card6_082() {
        super(Side.SHADOW, 0, Culture.RAIDER, "Trample", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canSpot(game, Culture.RAIDER, Race.MAN, Filters.mounted);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PreventableEffect(action,
                        new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Keyword.RING_BEARER)) {
                            @Override
                            public String getText(LotroGame game) {
                                return "Discard a companion (except the Ring-bearer)";
                            }
                        },
                        game.getGameState().getCurrentPlayerId(),
                        new PreventableEffect.PreventionCost() {
                            @Override
                            public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                return new AddTwilightEffect(self, 4);
                            }
                        }
                ));
        return action;
    }
}
