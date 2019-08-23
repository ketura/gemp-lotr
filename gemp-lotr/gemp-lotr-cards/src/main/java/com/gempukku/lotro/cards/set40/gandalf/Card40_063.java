package com.gempukku.lotro.cards.set40.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
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
 * Title: *Albert Dreary, Entertainer from Bree
 * Set: Second Edition
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Ally - Man - Bree
 * Strength: 3
 * Vitality: 3
 * Card Number: 1R63
 * Game Text: To play, spot Gandalf.
 * Maneuver: Exert Albert Dreary to discard an [ISENGARD] or [MORIA] condition.
 */
public class Card40_063 extends AbstractAlly{
    public Card40_063() {
        super(1, SitesBlock.SECOND_ED, 0, 3, 3, Race.MAN, Culture.GANDALF, "Albert Dreary",
                "Entertainer from Bree", true);
        addKeyword(Keyword.BREE);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.gandalf);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1,
                            CardType.CONDITION, Filters.or(Culture.ISENGARD, Culture.MORIA)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
