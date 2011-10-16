package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Arwen. She is damage +1. Skirmish: Exert Arwen or discard 2 cards from hand to make her
 * strength +1.
 */
public class Card1_047 extends AbstractAttachableFPPossession {
    public Card1_047() {
        super(2, 2, 0, Culture.ELVEN, PossessionClass.HAND_WEAPON, "Gwemegil", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Arwen");
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && (PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())
                || game.getGameState().getHand(playerId).size() >= 2)) {
            final ActivateCardAction action = new ActivateCardAction(self);

            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ExertCharactersEffect(self, self.getAttachedTo()));
            possibleCosts.add(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2));

            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.sameCard(self.getAttachedTo()), 1),
                            Phase.SKIRMISH));

            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE));
    }
}
