package com.gempukku.lotro.cards.set30.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 7
 * Vitality: 4
 * Game Text: Damage +1. Thorin is strength +1 for each [DWARVEN] follower he bears. Fellowship: Discard a [DWARVEN]
 * follower to place a [DWARVEN] artifact from your discard pile beneath your draw deck.
 */
public class Card30_019 extends AbstractCompanion {
    public Card30_019() {
        super(2, 7, 4, 6, Culture.DWARVEN, Race.DWARF, null, "Thorin", "Oakenshield", true);
        addKeyword(Keyword.DAMAGE, 1);
    }


    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, self, null, new CountActiveEvaluator(Filters.attachedTo(self), Culture.DWARVEN, CardType.FOLLOWER)));
    }
	
	@Override
	protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
				&& PlayConditions.canDiscardFromPlay(self, game, 1, Culture.DWARVEN, CardType.FOLLOWER)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
					new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.DWARVEN, CardType.FOLLOWER));
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose DWARVEN artifact", game.getGameState().getDiscard(playerId), Filters.and(Culture.DWARVEN, CardType.ARTIFACT), 1, 1) {
				@Override
				protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
					for (PhysicalCard selectedCard : selectedCards) {
						action.appendEffect(
								new PutCardFromDiscardOnBottomOfDeckEffect(selectedCard));
					}
				}
			});
            return Collections.singletonList(action);
        }
        return null;
	}
}