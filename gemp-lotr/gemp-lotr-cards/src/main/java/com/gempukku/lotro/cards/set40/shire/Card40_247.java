package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Farmer Maggot, Chaser of Rascals
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Ally - Hobbit - Shire
 * Strength: 2
 * Vitality: 3
 * Card Number: 1R247
 * Game Text: Fellowship: Exert Farmer Maggot to heal Merry or Pippin.
 */
public class Card40_247 extends AbstractAlly {
    public Card40_247() {
        super(1, SitesBlock.SECOND_ED, 0, 2, 3, Race.HOBBIT, Culture.SHIRE, "Farmer Maggot", "Chaser of Rascals", true);
        addKeyword(Keyword.SHIRE);
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
        && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Filters.or(Filters.name("Merry"), Filters.name("Pippin"))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
