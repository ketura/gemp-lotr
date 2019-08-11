package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Bilbo, Well-Preserved Hobbit
 * Shire	Ally • Hobbit • Shire
 * 3	3
 * Fellowship: Exert Bilbo and discard a pipeweed possession to discard a Shadow condition.
 */
public class Card20_382 extends AbstractAlly {
    public Card20_382() {
        super(2, null, 0, 3, 3, Race.HOBBIT, Culture.SHIRE, "Bilbo", "Well-Preserved Hobbit", true);
        addKeyword(Keyword.SHIRE);
    }

    @Override
    protected List<ActivateCardAction> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canDiscardFromPlay(self, game, Keyword.PIPEWEED, CardType.POSSESSION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Keyword.PIPEWEED, CardType.POSSESSION));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.SHADOW, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
