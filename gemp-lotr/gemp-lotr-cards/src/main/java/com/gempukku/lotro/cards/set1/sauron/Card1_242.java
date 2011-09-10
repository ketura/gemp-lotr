package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDeckIntoHandOrDiscardEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
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
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Search. To play, spot a [SAURON] Orc. Plays to your support area. Shadow: Remove (3) to reveal the top
 * card of your draw deck. If it is a [SAURON] card, take it into hand. Otherwise, discard it and one other card
 * from hand.
 */
public class Card1_242 extends AbstractLotroCardBlueprint {
    public Card1_242() {
        super(Side.SHADOW, CardType.CONDITION, Culture.SAURON, "The Dark Lord's Summons");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.canPayForShadowCard(game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.keyword(Keyword.ORC));
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.SHADOW_SUPPORT, twilightModifier);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, Phase.SHADOW, self)
                && checkPlayRequirements(playerId, game, self, 0))
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));

        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 3)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.SHADOW, "Remove (3) to reveal " +
                    "the top card of your draw deck. If it is a [SAURON] card, take it into hand. Otherwise, discard it " +
                    "and one other card from hand.");
            action.addCost(new RemoveTwilightEffect(3));
            action.addEffect(
                    new UnrespondableEffect() {
                        @Override
                        public void playEffect(LotroGame game) {
                            List<? extends PhysicalCard> deck = game.getGameState().getDeck(playerId);
                            if (deck.size() > 0) {
                                PhysicalCard topCard = deck.get(0);
                                if (topCard.getBlueprint().getCulture() == Culture.SAURON)
                                    action.addEffect(
                                            new PutCardFromDeckIntoHandOrDiscardEffect(topCard));
                                else {
                                    action.addEffect(
                                            new DiscardCardFromDeckEffect(playerId, topCard));
                                    action.addEffect(
                                            new ChooseAndDiscardCardsFromHandEffect(action, playerId, false));
                                }
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
