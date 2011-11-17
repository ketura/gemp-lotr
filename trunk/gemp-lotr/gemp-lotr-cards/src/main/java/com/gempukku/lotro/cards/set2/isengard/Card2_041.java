package com.gempukku.lotro.cards.set2.isengard;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ForEachBurdenYouSpotEffect;
import com.gempukku.lotro.cards.effects.ShuffleCardsFromDiscardIntoDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.Collection;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Shadow: Exert an Uruk-hai and spot X burdens to shuffle X minions from your discard pile into your draw
 * deck.
 */
public class Card2_041 extends AbstractOldEvent {
    public Card2_041() {
        super(Side.SHADOW, Culture.ISENGARD, "Evil Afoot", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.URUK_HAI));
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.URUK_HAI)));
        action.appendEffect(
                new ForEachBurdenYouSpotEffect(playerId) {
                    @Override
                    protected void burdensSpotted(int burdensSpotted) {
                        action.insertEffect(
                                new ChooseCardsFromDiscardEffect(playerId, burdensSpotted, burdensSpotted, CardType.MINION) {
                                    @Override
                                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                                        action.insertEffect(
                                                new ShuffleCardsFromDiscardIntoDeckEffect(self, playerId, cards));
                                    }
                                });
                    }
                });
        return action;
    }
}
