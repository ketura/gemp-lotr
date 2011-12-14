package com.gempukku.lotro.cards.set12.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 3
 * Site: 3
 * Game Text: Shadow: Exert Ulaire Otsea and discard a [WRAITH] condition from hand to discard a Free Peoples condition
 * from play.
 */
public class Card12_179 extends AbstractMinion {
    public Card12_179() {
        super(4, 9, 3, 3, Race.NAZGUL, Culture.WRAITH, "Ulaire Otsea", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Culture.WRAITH, CardType.CONDITION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.WRAITH, CardType.CONDITION));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.CONDITION, Side.FREE_PEOPLE));
            return Collections.singletonList(action);
        }
        return null;
    }
}
