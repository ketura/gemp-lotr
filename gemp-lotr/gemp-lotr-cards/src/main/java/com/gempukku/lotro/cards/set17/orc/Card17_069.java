package com.gempukku.lotro.cards.set17.orc;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.RevealHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardsFromHandBeneathDrawDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Each time a mounted [ORC] Orc wins a skirmish, the Free Peoples player must reveal his or her hand
 * and place two Free Peoples cards revealed on the bottom of his or her draw deck.
 */
public class Card17_069 extends AbstractPermanent {
    public Card17_069() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.ORC, "Cry and Panic");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Culture.ORC, Race.ORC, Filters.mounted)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new RevealHandEffect(self, self.getOwner(), GameUtils.getFreePeoplePlayer(game)) {
                        @Override
                        protected void cardsRevealed(Collection<? extends PhysicalCard> cards) {
                            action.appendEffect(
                                    new ChooseAndPutCardsFromHandBeneathDrawDeckEffect(action, game.getGameState().getCurrentPlayerId(), 2, Filters.in(cards), Side.FREE_PEOPLE));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
