package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.HealCharactersEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Rosie Cotton, Green Dragon Barkeet
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Ally - Hobbit - Shire
 * Strength: 2
 * Vitality: 2
 * Card Number: 1C266
 * Game Text: Fellowship: Exert Rosie Cotton to heal Sam.
 * Skirmish: Exert Rosie Cotton to make Sam strength +2.
 */
public class Card40_266 extends AbstractAlly {
    public Card40_266() {
        super(1, SitesBlock.SECOND_ED, 0, 2, 2, Race.HOBBIT, Culture.SHIRE, "Rosie Cotton",
                "Green Dragon Barkeep", true);
        addKeyword(Keyword.SHIRE);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new HealCharactersEffect(self, Filters.sam));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 2, Filters.sam));
            return Collections.singletonList(action);
        }
        return null;
    }
}
