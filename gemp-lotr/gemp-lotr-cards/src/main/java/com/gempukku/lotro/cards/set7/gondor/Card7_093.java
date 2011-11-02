package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ResolveSkirmishEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession • Armor
 * Game Text: Bearer must be a [GONDOR] Man. Response: if bearer is about to be overwhelmed, discard a [GONDOR]
 * fortification or a card from hand to make bearer strength +2.
 */
public class Card7_093 extends AbstractAttachableFPPossession {
    public Card7_093() {
        super(1, 0, 0, Culture.GONDOR, PossessionClass.ARMOR, "Footman's Armor");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    public List<? extends Action> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == Effect.Type.BEFORE_SKIRMISH_RESOLVED) {
            ResolveSkirmishEffect resolveEffect = (ResolveSkirmishEffect) effect;
            if (resolveEffect.getUpcomingResult(game) == ResolveSkirmishEffect.Result.FELLOWSHIP_OVERWHELMED
                    && game.getGameState().getSkirmish().getFellowshipCharacter() == self.getAttachedTo()) {
                if (PlayConditions.canDiscardFromPlay(self, game, Culture.GONDOR, Keyword.FORTIFICATION)
                        || PlayConditions.canDiscardFromHand(game, playerId, 1, Filters.any)) {
                    ActivateCardAction action = new ActivateCardAction(self);
                    List<Effect> possibleCosts = new LinkedList<Effect>();
                    possibleCosts.add(
                            new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.GONDOR, Keyword.FORTIFICATION) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Discard a GONDOR Fortification";
                                }
                            });
                    possibleCosts.add(
                            new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Discard a card from hand";
                                }
                            });
                    action.appendCost(
                            new ChoiceEffect(action, playerId, possibleCosts));
                    action.appendEffect(
                            new AddUntilEndOfPhaseModifierEffect(
                                    new StrengthModifier(self, Filters.hasAttached(self), 2), Phase.SKIRMISH));
                    return Collections.singletonList(action);
                }
            }
        }
        return null;
    }
}
