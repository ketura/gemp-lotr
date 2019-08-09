package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.ReplaceInSkirmishEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Horn of Gondor
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Possession - Horn
 * Card Number: 1U109
 * Game Text: Bearer must be a [GONDOR] Man.
 * Skirmish: Exert bearer twice to have another companion replace this companion in this skirmish.
 */
public class Card40_109 extends AbstractAttachableFPPossession {
    public Card40_109() {
        super(0, 0, 0, Culture.GONDOR, PossessionClass.HORN, "Horn of Gondor", null, true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, 2, Filters.hasAttached(self))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.hasAttached(self)));
            if (PlayConditions.isActive(game, Filters.inSkirmish, Filters.hasAttached(self)))
                action.appendEffect(
                        new ChooseActiveCardEffect(self, playerId, "Choose a companion to replace in skirmish", CardType.COMPANION, Filters.notAssignedToSkirmish) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                action.appendEffect(
                                        new ReplaceInSkirmishEffect(card, Filters.hasAttached(self)));
                            }
                        });
            return Collections.singletonList(action);
        }
        return null;
    }
}
