package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.cards.modifiers.HasInitiativeModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

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
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "Feel His Blade");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && self.getZone() == Zone.SUPPORT
                && Filters.filter(game.getGameState().getSkirmish().getShadowCharacters(), game.getGameState(), game.getModifiersQuerying(), Race.NAZGUL).size() > 0
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
