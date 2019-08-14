package com.gempukku.lotro.cards.set12.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.TransferPermanentEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.RemoveSpecialAbilitiesModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Skirmish: Transfer this condition from your support area to a character skirmishing a Nazgul. The Free
 * Peoples player cannot use bearer's special abilities.
 */
public class Card12_169 extends AbstractPermanent {
    public Card12_169() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, "Sauron's Gaze");
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && self.getZone() == Zone.SUPPORT
                && Filters.filter(game.getGameState().getSkirmish().getShadowCharacters(), game, Race.NAZGUL).size() > 0
                && game.getGameState().getSkirmish().getFellowshipCharacter() != null) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new TransferPermanentEffect(self, game.getGameState().getSkirmish().getFellowshipCharacter()));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new RemoveSpecialAbilitiesModifier(self, Filters.hasAttached(self)));
}
}
