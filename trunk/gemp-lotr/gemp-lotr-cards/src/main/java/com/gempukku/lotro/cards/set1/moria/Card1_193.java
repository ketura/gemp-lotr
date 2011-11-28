package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Response: If your [MORIA] weapon is discarded, play it from your discard pile
 * (that weapon's twilight cost is -1).
 */
public class Card1_193 extends AbstractPermanent {
    public Card1_193() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.MORIA, Zone.SUPPORT, "Plundered Armories");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.forEachDiscardedFromPlay(game, effectResult, Culture.MORIA, Filters.weapon, Zone.DISCARD, Filters.playable(game, -1))) {
            DiscardCardsFromPlayResult discardResult = (DiscardCardsFromPlayResult) effectResult;
            final PhysicalCard discardedCard = discardResult.getDiscardedCard();
            ActivateCardAction action = new ActivateCardAction(self);
            action.setText("Play " + GameUtils.getCardLink(discardedCard));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId,
                            game,
                            -1, Filters.and(
                            Culture.MORIA,
                            Filters.weapon,
                            discardedCard)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
