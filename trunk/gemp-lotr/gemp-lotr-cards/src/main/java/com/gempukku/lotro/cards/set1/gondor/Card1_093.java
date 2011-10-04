package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Exert Aragorn to make Arwen strength +3, or exert Arwen to make Aragorn strength +3.
 */
public class Card1_093 extends AbstractEvent {
    public Card1_093() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Arwen's Fate", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.or(Filters.name("Aragorn"), Filters.name("Arwen")));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PhysicalCard arwen = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Arwen"));
        final PhysicalCard aragorn = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.name("Aragorn"));

        final PlayEventAction action = new PlayEventAction(self);

        List<Effect> possibleEffects = new LinkedList<Effect>();

        if (arwen != null) {
            possibleEffects.add(
                    new ExertCharactersEffect(self, arwen) {
                        @Override
                        protected FullEffectResult playEffectReturningResult(LotroGame game) {
                            FullEffectResult effectResult = super.playEffectReturningResult(game);
                            if (effectResult.isSuccessful() && aragorn != null) {
                                action.appendEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new StrengthModifier(self, Filters.sameCard(aragorn), 3), Phase.SKIRMISH));
                            }
                            return effectResult;
                        }
                    });
        }
        if (aragorn != null) {
            possibleEffects.add(
                    new ExertCharactersEffect(self, aragorn) {
                        @Override
                        protected FullEffectResult playEffectReturningResult(LotroGame game) {
                            FullEffectResult effectResult = super.playEffectReturningResult(game);
                            if (effectResult.isSuccessful() && arwen != null) {
                                action.appendEffect(
                                        new AddUntilEndOfPhaseModifierEffect(
                                                new StrengthModifier(self, Filters.sameCard(arwen), 3), Phase.SKIRMISH));
                            }
                            return effectResult;
                        }
                    });
        }

        action.appendCost(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
