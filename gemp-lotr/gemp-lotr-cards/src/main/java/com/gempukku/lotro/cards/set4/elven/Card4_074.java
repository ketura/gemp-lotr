package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Archer.
 * Skirmish: Exert Legolas to make a minion skirmishing an unbound companion strength -1 (or -2 if that companion
 * is Gimli).
 */
public class Card4_074 extends AbstractCompanion {
    public Card4_074() {
        super(2, 6, 3, 6, Culture.ELVEN, Race.ELF, Signet.GANDALF, "Legolas", true);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose unbound companion", Filters.unboundCompanion) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            final int bonus = (card.getBlueprint().getName().equals("Gimli")) ? -2 : -1;
                            action.insertEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose minion skirmishing companion", CardType.MINION, Filters.inSkirmishAgainst(Filters.sameCard(card))) {
                                        @Override
                                        protected void cardSelected(LotroGame game, PhysicalCard minion) {
                                            action.insertEffect(
                                                    new AddUntilEndOfPhaseModifierEffect(
                                                            new StrengthModifier(self, Filters.sameCard(minion), bonus), Phase.SKIRMISH));
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
