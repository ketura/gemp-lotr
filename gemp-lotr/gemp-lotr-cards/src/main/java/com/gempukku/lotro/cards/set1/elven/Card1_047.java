package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardFromHandEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.modifiers.CompositeModifier;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

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
        super(2, Culture.ELVEN, "Gwemegil", true);
        addKeyword(Keyword.HAND_WEAPON);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.name("Arwen"), Filters.not(Filters.hasAttached(Filters.keyword(Keyword.HAND_WEAPON))));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && (PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self.getAttachedTo())
                || game.getGameState().getHand(playerId).size() >= 2)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.SKIRMISH, "Exert Arwen or discard 2 cards from hand to make her Strength +1");

            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ExertCharacterEffect(self.getAttachedTo()));
            possibleCosts.add(
                    new UnrespondableEffect() {
                        @Override
                        public String getText() {
                            return "Discard 2 cards";
                        }

                        @Override
                        public boolean canPlayEffect(LotroGame game) {
                            return game.getGameState().getHand(playerId).size() >= 2;
                        }

                        @Override
                        public void playEffect(LotroGame game) {
                            action.addCost(new ChooseAndDiscardCardFromHandEffect(action, playerId, true));
                            action.addCost(new ChooseAndDiscardCardFromHandEffect(action, playerId, true));
                        }
                    });

            action.addCost(
                    new ChoiceEffect(action, playerId, possibleCosts, true));
            action.addEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.sameCard(self.getAttachedTo()), 1),
                            Phase.SKIRMISH));

            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new StrengthModifier(null, null, 2));
        modifiers.add(new KeywordModifier(null, null, Keyword.DAMAGE));

        return new CompositeModifier(self, Filters.attachedTo(self), modifiers);
    }
}
