package com.gempukku.lotro.cards.set7.gondor;

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
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 5
 * Type: Condition • Support Area
 * Strength: -2
 * Vitality: -2
 * Game Text: Fortification. Skirmish: Exert 3 [GONDOR] Men to transfer this condition from your support area to
 * a minion skirmishing a [GONDOR] Man.
 */
public class Card7_119 extends AbstractPermanent {
    public Card7_119() {
        super(Side.FREE_PEOPLE, 5, CardType.CONDITION, Culture.GONDOR, "Seventh Level", null, true);
        addKeyword(Keyword.FORTIFICATION);
    }

    @Override
    public int getStrength() {
        return -2;
    }

    @Override
    public int getVitality() {
        return -2;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && self.getZone() == Zone.SUPPORT
                && PlayConditions.canExert(self, game, 1, 3, Culture.GONDOR, Race.MAN)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 3, 3, Culture.GONDOR, Race.MAN));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", CardType.MINION, Filters.inSkirmishAgainst(Culture.GONDOR, Race.MAN)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new TransferPermanentEffect(self, card));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
