package com.gempukku.lotro.cards.set3.dwarven;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ExertCharactersCost;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.KeywordSpotModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

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
        super(Side.FREE_PEOPLE, CardType.POSSESSION, 1, Culture.DWARVEN, Keyword.PIPE, "Gimli's Pipe", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Gimli");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.FELLOWSHIP);
            action.appendCost(
                    new ExertCharactersCost(self, self.getAttachedTo()));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new KeywordSpotModifier(self, Keyword.PIPE), Phase.FELLOWSHIP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
