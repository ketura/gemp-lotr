package com.gempukku.lotro.cards.set5.rohan;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Ally • Home 4T • Man
 * Strength: 4
 * Vitality: 2
 * Site: 4T
 * Game Text: Villager. Fellowship: Exert Ecglaf and spot 2 valiant Men to play a [ROHAN] possession from your
 * discard pile.
 */
public class Card5_081 extends AbstractAlly {
    public Card5_081() {
        super(1, Block.TWO_TOWERS, 4, 4, 2, Race.MAN, Culture.ROHAN, "Ecglaf", true);
        addKeyword(Keyword.VILLAGER);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canSpot(game, 2, Keyword.VALIANT, Race.MAN)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.ROHAN, CardType.POSSESSION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId), Culture.ROHAN, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
