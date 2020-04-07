package com.gempukku.lotro.cards.set15.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.TransferPermanentEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 4
 * Type: Condition • Support Area
 * Strength: -1
 * Vitality: -2
 * Game Text: Fortification. This condition is twilight cost -1 for each [GONDOR] ranger you spot.
 * Skirmish: Exert 2 [GONDOR] Men to transfer this condition from your support area to a minion skirmishing
 * a [GONDOR] Man.
 */
public class Card15_058 extends AbstractPermanent {
    public Card15_058() {
        super(Side.FREE_PEOPLE, 4, CardType.CONDITION, Culture.GONDOR, "Decorated Barricade", null, true);
        addKeyword(Keyword.FORTIFICATION);
    }

    @Override
    public int getStrength() {
        return -1;
    }

    @Override
    public int getVitality() {
        return -2;
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self, PhysicalCard target) {
        return -Filters.countActive(game, Culture.GONDOR, Keyword.RANGER);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, 1, 2, Culture.GONDOR, Race.MAN)
                && self.getZone() == Zone.SUPPORT) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 2, 2, Culture.GONDOR, Race.MAN));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION, Filters.inSkirmishAgainst(Culture.GONDOR, Race.MAN)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new TransferPermanentEffect(self, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
