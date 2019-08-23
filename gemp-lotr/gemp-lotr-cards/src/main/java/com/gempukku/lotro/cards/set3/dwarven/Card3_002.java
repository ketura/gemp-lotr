package com.gempukku.lotro.cards.set3.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.PossessionClassSpotModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Possession • Pipe
 * Game Text: Bearer must be Gimli. Fellowship: Exert Gimli to add 1 to the number of pipes you can spot.
 */
public class Card3_002 extends AbstractAttachable {
    public Card3_002() {
        super(Side.FREE_PEOPLE, CardType.POSSESSION, 1, Culture.DWARVEN, PossessionClass.PIPE, "Gimli's Pipe", null, true);
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.gimli;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, self.getAttachedTo())) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(action, self, self.getAttachedTo()));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new PossessionClassSpotModifier(self, PossessionClass.PIPE)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
