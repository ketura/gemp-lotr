package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.PutCardFromDeckIntoHandEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.RevealBottomCardsOfDrawDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.CantReplaceSiteByFPPlayerModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: While you can spot a [ORC] Orc, the Free Peoples player cannot replace the fellowship’s current site.
 * Shadow: Exert an [ORC] minion and remove (2) to reveal the bottom card of your draw deck. If it is an [ORC] Orc,
 * take it into hand. Otherwise, discard it.
 */
public class Card15_113 extends AbstractPermanent {
    public Card15_113() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.ORC, "Orkish Camp");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new CantReplaceSiteByFPPlayerModifier(self, new SpotCondition(Culture.ORC, Race.ORC), Filters.currentSite));
}

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 2)
                && PlayConditions.canExert(self, game, Culture.ORC, CardType.MINION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(2));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.ORC, CardType.MINION));
            action.appendEffect(
                    new RevealBottomCardsOfDrawDeckEffect(self, playerId, 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                            for (PhysicalCard card : revealedCards) {
                                if (Filters.and(Culture.ORC, Race.ORC).accepts(game, card))
                                    action.appendEffect(
                                            new PutCardFromDeckIntoHandEffect(card));
                                else
                                    action.appendEffect(
                                            new DiscardCardFromDeckEffect(card));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
