package com.gempukku.lotro.cards.set40.shire;

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
 * Title: *Bilbo, Well-preserved Hobbit
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Ally - Hobbit - Shire
 * Strength: 3
 * Vitality: 3
 * Card Number: 1R243
 * Game Text: Fellowship: Exert Bilbo and discard a pipeweed possession to discard a Shadow condition.
 */
public class Card40_243 extends AbstractAlly {
    public Card40_243() {
        super(2, SitesBlock.SECOND_ED, 0, 3, 3, Race.HOBBIT, Culture.SHIRE, "Bilbo", "Well-preserved Hobbit", true);
        addKeyword(Keyword.SHIRE);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canDiscardFromPlay(self, game, CardType.POSSESSION, Keyword.PIPEWEED)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION, Keyword.PIPEWEED));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.SHADOW, CardType.CONDITION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
