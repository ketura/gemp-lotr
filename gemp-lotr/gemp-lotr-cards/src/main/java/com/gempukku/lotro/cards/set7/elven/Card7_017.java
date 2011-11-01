package com.gempukku.lotro.cards.set7.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Possession • Mount
 * Strength: +2
 * Game Text: Bearer must be an Elf. Skirmish: If bearer is Arwen, exert her to make each minion skirmishing her
 * strength -2.
 */
public class Card7_017 extends AbstractAttachableFPPossession {
    public Card7_017() {
        super(2, 2, 0, Culture.ELVEN, PossessionClass.MOUNT, "Asfaloth", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.ELF;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, Filters.arwen, Filters.hasAttached(self))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(Filters.hasAttached(self))), -2), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
