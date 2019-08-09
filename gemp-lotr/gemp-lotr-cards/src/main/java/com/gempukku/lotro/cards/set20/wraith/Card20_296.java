package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndStackCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * 0
 * •Nine for Mortal Men
 * Artifact • Support Area
 * Shadow: Stack a [Ringwraith] card here.
 * Response: If a Nazgul is about to take a wound, discard a card stacked here to prevent that wound.
 * Regroup: Discard 3 cards stacked here to take a [Ringwraith] card from your discard pile into hand.
 * http://lotrtcg.org/coreset/ringwraith/nineformortalmen(r2).jpg
 */
public class Card20_296 extends AbstractPermanent {
    public Card20_296() {
        super(Side.SHADOW, 0, CardType.ARTIFACT, Culture.WRAITH, "Nine for Mortal Men", null, true);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Race.NAZGUL)
                && game.getGameState().getStackedCards(self).size()>0) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, self, Filters.any));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, playerId, "Choose Nazgul to prevent wound to", Race.NAZGUL));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.hasCardInHand(game, playerId, 1, Culture.WRAITH)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndStackCardsFromHandEffect(action, playerId, 1, 1, self, Culture.WRAITH));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
            && game.getGameState().getStackedCards(self).size()>=3) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 3, 3, self, Filters.any));
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.WRAITH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
