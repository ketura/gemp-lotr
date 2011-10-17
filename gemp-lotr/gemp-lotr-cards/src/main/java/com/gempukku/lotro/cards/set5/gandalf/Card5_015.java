package com.gempukku.lotro.cards.set5.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.ParticipatesInSkirmishesModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 3
 * Type: Companion • Ent
 * Strength: 8
 * Vitality: 4
 * Resistance: 6
 * Game Text: Unhasty. Assignment: Exert an unbound Hobbit or discard 2 cards from hand to allow Birchseed to skirmish.
 * Fellowship: Exert Birchseed to Heal an unbound Hobbit.
 */
public class Card5_015 extends AbstractCompanion {
    public Card5_015() {
        super(3, 8, 4, Culture.GANDALF, Race.ENT, null, "Birchseed", true);
        addKeyword(Keyword.UNHASTY);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Race.HOBBIT, Filters.unboundCompanion));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.ASSIGNMENT, self)
                && (
                PlayConditions.canExert(self, game, Race.HOBBIT, Filters.unboundCompanion)
                        || game.getGameState().getHand(playerId).size() >= 2)) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.HOBBIT, Filters.unboundCompanion));
            possibleCosts.add(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2));
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ParticipatesInSkirmishesModifier(self, Filters.sameCard(self)), Phase.ASSIGNMENT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
