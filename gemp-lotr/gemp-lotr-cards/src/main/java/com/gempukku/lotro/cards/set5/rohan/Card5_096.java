package com.gempukku.lotro.cards.set5.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.RevealCardsFromHandResult;

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
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose companion or ally",
                        Filters.or(CardType.COMPANION, CardType.ALLY), Filters.inSkirmishAgainst(Culture.SAURON, Race.ORC)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        int ringBoundCompanions = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION, Keyword.RING_BOUND);
                        action.insertEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(card), -ringBoundCompanions), Phase.SKIRMISH));
                    }
                });
        return action;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.REVEAL_CARDS_FROM_HAND) {
            RevealCardsFromHandResult revealResult = (RevealCardsFromHandResult) effectResult;
            if (revealResult.getSource().getOwner().equals(game.getGameState().getCurrentPlayerId())
                    && revealResult.getRevealedCards().contains(self)
                    && PlayConditions.canPayForShadowCard(game, self, 0)) {
                PlayEventAction action = new PlayEventAction(self);
                action.appendEffect(
                        new AddBurdenEffect(self, 2));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
