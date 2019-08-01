package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.OptionalEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Silent Scouts
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Condition - Support Area
 * Card Number: 1U58
 * Game Text: Each time the fellowship moves during the regroup hpase, reveal the top card of your draw deck. If it is
 * an [ELVEN] card, you may make the Shadow player wound a minion.
 */
public class Card40_058 extends AbstractPermanent{
    public Card40_058() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ELVEN, Zone.SUPPORT, "Silent Scouts", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)
                && PlayConditions.isPhase(game, Phase.REGROUP)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(self, self.getOwner(), 1) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                            if (revealedCards.size()>0) {
                                PhysicalCard revealedCard = revealedCards.get(0);
                                if (revealedCard.getBlueprint().getCulture() == Culture.ELVEN) {
                                    action.appendEffect(
                                            new OptionalEffect(action, self.getOwner(),
                                                    new ChooseAndWoundCharactersEffect(action, GameUtils.getShadowPlayers(game)[0], 1, 1, CardType.MINION) {
                                                        @Override
                                                        public String getText(LotroGame game) {
                                                            return "Make the Shadow player wound a minion";
                                                        }
                                                    }));
                                }
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
