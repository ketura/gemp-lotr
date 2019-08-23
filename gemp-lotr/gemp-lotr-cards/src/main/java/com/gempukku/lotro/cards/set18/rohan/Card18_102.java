package com.gempukku.lotro.cards.set18.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: To play, spot Theoden. Skirmish: Spot a [ROHAN] Man bearing a possession and discard a [ROHAN] possession
 * from hand to make that Man strength +2.
 */
public class Card18_102 extends AbstractPermanent {
    public Card18_102() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.ROHAN, "Rohirrim Diadem");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.name(Names.theoden));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSpot(game, Culture.ROHAN, Race.MAN, Filters.hasAttached(CardType.POSSESSION))
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Culture.ROHAN, CardType.POSSESSION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseActiveCardEffect(self, playerId, "Choose a ROHAN Man bearing a possession", Culture.ROHAN, Race.MAN, Filters.hasAttached(CardType.POSSESSION)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, card, 2)));
                        }
                    });
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.ROHAN, CardType.POSSESSION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
