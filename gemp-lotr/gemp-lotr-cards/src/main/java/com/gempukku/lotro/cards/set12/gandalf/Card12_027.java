package com.gempukku.lotro.cards.set12.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ReplaceInSkirmishEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Wizard
 * Strength: 7
 * Vitality: 4
 * Resistance: 7
 * Game Text: Skirmish: If Gandalf is not assigned to a skirmish, exert him and another unbound companion skirmishing
 * a minion to make Gandalf strength +3 and have him replace that companion in that skirmish.
 */
public class Card12_027 extends AbstractCompanion {
    public Card12_027() {
        super(4, 7, 4, 7, Culture.GANDALF, Race.WIZARD, null, "Gandalf", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && Filters.notAssignedToSkirmish.accepts(game.getGameState(), game.getModifiersQuerying(), self)
                && PlayConditions.canSelfExert(self, game)
                && PlayConditions.canExert(self, game, Filters.unboundCompanion, Filters.inSkirmishAgainst(CardType.MINION))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(self));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.unboundCompanion, Filters.inSkirmishAgainst(CardType.MINION)));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self, 3), Phase.SKIRMISH));
            action.appendEffect(
                    new ReplaceInSkirmishEffect(self, Filters.unboundCompanion, Filters.inSkirmishAgainst(CardType.MINION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
