package com.gempukku.lotro.cards.set5.dunland;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.ShuffleCardsFromHandIntoDeckEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.HashSet;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Shadow: Spot 3 [DUNLAND] minions to shuffle your hand into your draw deck and draw 8 cards.
 * The Free Peoples player may discard 3 cards from hand to prevent this.
 */
public class Card5_003 extends AbstractEvent {
    public Card5_003() {
        super(Side.SHADOW, 1, Culture.DUNLAND, "Leaping Blaze", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 3, Culture.DUNLAND, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PreventableEffect(action,
                        new UnrespondableEffect() {
                            @Override
                            protected void doPlayEffect(LotroGame game) {
                                action.appendEffect(
                                        new ShuffleCardsFromHandIntoDeckEffect(self, playerId, new HashSet<PhysicalCard>(game.getGameState().getHand(playerId))));
                                action.appendEffect(
                                        new DrawCardsEffect(action, playerId, 8));
                            }

                            @Override
                            public String getText(LotroGame game) {
                                return "Shuffle hand into draw deck and draw 8 cards";
                            }
                        }, game.getGameState().getCurrentPlayerId(),
                        new PreventableEffect.PreventionCost() {
                            @Override
                            public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                return new ChooseAndDiscardCardsFromHandEffect(subAction, playerId, false, 3);
                            }
                        }
                ));
        return action;
    }
}
