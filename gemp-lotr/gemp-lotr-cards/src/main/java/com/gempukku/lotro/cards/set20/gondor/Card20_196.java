package com.gempukku.lotro.cards.set20.gondor;

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
 * 0
 * •Horn of Gondor
 * Gondor	Possession •   Horn
 * Bearer must be a [Gondor] companion.
 * Skirmish: Exert bearer twice to have another companion replace this companion in this skirmish.
 */
public class Card20_196 extends AbstractAttachableFPPossession {
    public Card20_196() {
        super(0, 0, 0, Culture.GONDOR, PossessionClass.HORN, "Horn of Gondor", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, CardType.COMPANION);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, 2, Filters.hasAttached(self))) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.hasAttached(self)) {
                        @Override
                        protected void forEachCardExertedCallback(final PhysicalCard bearer) {
                            action.appendEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose companion", CardType.COMPANION, Filters.not(bearer)) {
                                        @Override
                                        protected void cardSelected(LotroGame game, PhysicalCard anotherCompanion) {
                                            action.appendEffect(
                                                    new ReplaceInSkirmishEffect(anotherCompanion, bearer));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
