package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDeckIntoHandOrDiscardEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Shadow: Remove (3) to reveal the bottom card of your draw deck. If it is a
 * [MORIA] Orc, take it into hand. Otherwise, discard it.
 */
public class Card1_200 extends AbstractPermanent {
    public Card1_200() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.MORIA, Zone.SHADOW_SUPPORT, "The Underdeeps of Moria");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 3)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.SHADOW, "Remove (3) to reveal the bottom card of your draw deck. If it is a MORIA Orc, take it into hand. Otherwise, discard it.");
            action.addCost(new RemoveTwilightEffect(3));
            action.addEffect(
                    new UnrespondableEffect() {
                        @Override
                        public void playEffect(LotroGame game) {
                            List<? extends PhysicalCard> deck = game.getGameState().getDeck(playerId);
                            if (deck.size() > 0) {
                                PhysicalCard bottomCard = deck.get(deck.size() - 1);
                                if (bottomCard.getBlueprint().getCulture() == Culture.MORIA
                                        && game.getModifiersQuerying().hasKeyword(game.getGameState(), bottomCard, Keyword.ORC)) {
                                    action.addEffect(
                                            new PutCardFromDeckIntoHandOrDiscardEffect(bottomCard));
                                } else {
                                    action.addEffect(
                                            new DiscardCardFromDeckEffect(playerId, bottomCard));
                                }
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
