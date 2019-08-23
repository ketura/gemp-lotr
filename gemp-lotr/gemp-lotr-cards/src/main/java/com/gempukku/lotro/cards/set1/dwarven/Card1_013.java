package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion � Dwarf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Damage +1. Skirmish: Exert Gimli to make him strength +2.
 */
public class Card1_013 extends AbstractCompanion {
    public Card1_013() {
        super(2, 6, 3, 6, Culture.DWARVEN, Race.DWARF, Signet.GANDALF, "Gimli", "Son of Gloin", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, self)) {
            ActivateCardAction action = new ActivateCardAction(self);

            action.appendCost(new SelfExertEffect(action, self));
            action.appendEffect(new AddUntilEndOfPhaseModifierEffect(new StrengthModifier(self, self, 2)));

            return Collections.singletonList(action);
        }
        return null;
    }
}
