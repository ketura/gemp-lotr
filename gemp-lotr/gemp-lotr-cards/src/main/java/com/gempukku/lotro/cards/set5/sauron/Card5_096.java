package com.gempukku.lotro.cards.set5.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.RevealCardFromHandResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Make a companion or ally skirmishing a [SAURON] Orc strength -1 for each ring-bound companion.
 * Response: If a free people player reveals this card from your hand, discard this card to add 2 burdens.
 */
public class Card5_096 extends AbstractEvent {
    public Card5_096() {
        super(Side.SHADOW, 0, Culture.SAURON, "Eye of Barad-Dur", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        if (game.getGameState().getCurrentPhase() == Phase.SKIRMISH) {
            final PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose companion or ally",
                            Filters.or(CardType.COMPANION, CardType.ALLY), Filters.inSkirmishAgainst(Culture.SAURON, Race.ORC)) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            int ringBoundCompanions = Filters.countActive(game, CardType.COMPANION, Keyword.RING_BOUND);
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(card), -ringBoundCompanions)));
                        }
                    });
            return action;
        }
        return null;
    }

    @Override
    public List<PlayEventAction> getOptionalInHandAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.REVEAL_CARDS_FROM_HAND) {
            RevealCardFromHandResult revealResult = (RevealCardFromHandResult) effectResult;
            if (revealResult.getSource().getOwner().equals(game.getGameState().getCurrentPlayerId())
                    && revealResult.getRevealedCard() == self
                    && checkResponsePlayRequirements(playerId, game, self, 0, 0, false, false)) {
                PlayEventAction action = new PlayEventAction(self);
                action.appendEffect(
                        new AddBurdenEffect(self.getOwner(), self, 2));
                return Collections.singletonList(action);
            }
        }
        return null;
    }

    private boolean checkResponsePlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        if (!game.getModifiersQuerying().canPayExtraCostsToPlay(game, self))
            return false;

        int toilCount = game.getModifiersQuerying().getKeywordCount(game, self, Keyword.TOIL);
        if (toilCount > 0)
            twilightModifier -= toilCount * Filters.countActive(game, Filters.owner(playerId), getCulture(), Filters.character, Filters.canExert(self));
        return (getSide() != Side.SHADOW || PlayConditions.canPayForShadowCard(game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty));
    }
}
