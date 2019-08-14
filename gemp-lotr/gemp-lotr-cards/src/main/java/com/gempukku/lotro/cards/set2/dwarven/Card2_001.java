package com.gempukku.lotro.cards.set2.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Tale. Plays to your support area. Fellowship: Discard the top 3 cards from your draw deck to play
 * a [DWARVEN] weapon from your discard pile.
 */
public class Card2_001 extends AbstractPermanent {
    public Card2_001() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.DWARVEN, "Beneath the Mountains");
        addKeyword(Keyword.TALE);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return game.getGameState().getDeck(self.getOwner()).size() >= 3;
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.DWARVEN, Filters.weapon)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardTopCardFromDeckEffect(self, playerId, 3, false));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.and(Culture.DWARVEN, Filters.weapon)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
