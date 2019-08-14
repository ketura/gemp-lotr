package com.gempukku.lotro.cards.set2.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ForEachBurdenYouSpotEffect;
import com.gempukku.lotro.logic.effects.ShuffleCardsFromDiscardIntoDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseCardsFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
public class Card2_041 extends AbstractEvent {
    public Card2_041() {
        super(Side.SHADOW, 0, Culture.ISENGARD, "Evil Afoot", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Race.URUK_HAI);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.URUK_HAI));
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
