package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.logic.effects.TransferPermanentEffect;
import com.gempukku.lotro.logic.modifiers.HasInitiativeModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: Skirmish: Transfer this condition from your support area to a character skirmishing a Nazgul. During the
 * turn this card is transferred, the Shadow has initiative, regardless of the Free Peoples player's hand.
 */
public class Card7_177 extends AbstractPermanent {
    public Card7_177() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.WRAITH, "Feel His Blade");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && self.getZone() == Zone.SUPPORT
                && Filters.filter(game.getGameState().getSkirmish().getShadowCharacters(), game, Race.NAZGUL).size() > 0
                && game.getGameState().getSkirmish().getFellowshipCharacter() != null) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new TransferPermanentEffect(self, game.getGameState().getSkirmish().getFellowshipCharacter()));
            action.appendEffect(
                    new AddUntilEndOfTurnModifierEffect(
                            new HasInitiativeModifier(self, null, Side.SHADOW)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
