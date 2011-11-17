package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be an Elf.
 * Skirmish: Exert bearer or discard 2 cards from hand to make a minion skirmishing bearer strength -1.
 */
public class Card4_064 extends AbstractAttachableFPPossession {
    public Card4_064() {
        super(1, 1, 0, Culture.ELVEN, PossessionClass.HAND_WEAPON, "Elven Sword");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.ELF;
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && (PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())
                || game.getGameState().getHand(playerId).size() >= 2)) {
            final ActivateCardAction action = new ActivateCardAction(self);

            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ExertCharactersEffect(self, self.getAttachedTo()) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert bearer";
                        }
                    });
            possibleCosts.add(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard 2 cards from hand";
                        }
                    });

            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose a minion", Filters.inSkirmishAgainst(Filters.hasAttached(self))) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(card), -1), Phase.SKIRMISH));
                        }
                    });

            return Collections.singletonList(action);
        }
        return null;
    }
}
