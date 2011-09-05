package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Possession
 * Game Text: Pipeweed. Plays to your support area. When you play this possession, you may discard up to 2 cards from
 * hand.
 */
public class Card1_300 extends AbstractLotroCardBlueprint {
    public Card1_300() {
        super(Side.FREE_PEOPLE, CardType.POSSESSION, Culture.SHIRE, "Longbottom Leaf");
        addKeyword(Keyword.PIPEWEED);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self) {
        return new PlayPermanentAction(self, Zone.FREE_SUPPORT);
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self));
        }
        return null;
    }

    @Override
    public List<? extends Action> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            final CostToEffectAction action = new CostToEffectAction(self, null, "You may discard up to 2 cards from hand.");
            action.addEffect(
                    new ChooseCardsFromHandEffect(playerId, "Choose up to 2 cards to discard from hand", 0, 2, Filters.any()) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            for (PhysicalCard selectedCard : selectedCards)
                                action.addEffect(new DiscardCardFromHandEffect(selectedCard));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
