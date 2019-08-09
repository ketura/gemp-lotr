package com.gempukku.lotro.cards.set8.shire;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: When the fellowship moves from site 4K, remove (1) for each Ring-bound companion you spot.
 * Regroup: Discard a card from hand for each Ring-bound companion you spot. Discard this condition.
 */
public class Card8_110 extends AbstractPermanent {
    public Card8_110() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Morgai Foothills", null, true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.movesFrom(game, effectResult, Filters.siteBlock(Block.KING), Filters.siteNumber(4))) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new ForEachYouSpotEffect(game.getGameState().getCurrentPlayerId(), CardType.COMPANION, Keyword.RING_BOUND) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            action.insertEffect(
                                    new RemoveTwilightEffect(spotCount));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ForEachYouSpotEffect(game.getGameState().getCurrentPlayerId(), CardType.COMPANION, Keyword.RING_BOUND) {
                        @Override
                        protected void spottedCards(int spotCount) {
                            action.insertEffect(
                                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, spotCount));
                            action.appendEffect(
                                    new SelfDiscardEffect(self));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
