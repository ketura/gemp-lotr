package com.gempukku.lotro.cards.set8.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Fellowship: Exert a [SHIRE] companion and either a [ROHAN] companion or a [GANDALF] companion to add a
 * [SHIRE] token here. Skirmish: Remove a [SHIRE] token here to prevent an unbound companion from being overwhelmed
 * unless his or her strength is tripled.
 */
public class Card8_112 extends AbstractPermanent {
    public Card8_112() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Song of the Shire");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game, Culture.SHIRE, CardType.COMPANION)
                && PlayConditions.canExert(self, game, CardType.COMPANION, Filters.or(Culture.ROHAN, Culture.GANDALF))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.SHIRE, CardType.COMPANION));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.or(Culture.ROHAN, Culture.GANDALF)));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.SHIRE));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canRemoveTokens(game, Token.SHIRE, 1, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTokenEffect(self, self, Token.SHIRE));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose unbound companion", Filters.unboundCompanion) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.insertEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new OverwhelmedByMultiplierModifier(self, card, 3), Phase.SKIRMISH));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
