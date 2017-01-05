package com.gempukku.lotro.cards.set31.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.*;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Artifact • Staff
 * Vitality: +1
 * Game Text: Bearer must be a Wizard. At the start of each of your turns, you may exert bearer to take a [GANDALF]
 * spell from your draw deck into your hand.
 */
public class Card31_018 extends AbstractAttachableFPPossession {
    public Card31_018() {
        super(2, 0, 1, Culture.GANDALF, CardType.ARTIFACT, PossessionClass.STAFF, "Wizard Staff", null, true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.WIZARD;
    }
	
	@Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfTurn(game, effectResult)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
			action.appendCost(new ExertCharactersEffect(action, self, self.getAttachedTo()));
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose GANDALF spell", game.getGameState().getDiscard(playerId), Filters.and(Culture.GANDALF, Keyword.SPELL), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            for (PhysicalCard selectedCard : selectedCards) {
                                action.appendEffect(
                                        new PutCardFromDiscardIntoHandEffect(selectedCard));
                            }
                        }
            });
            return Collections.singletonList(action);
        }
        return null;
	}
}
