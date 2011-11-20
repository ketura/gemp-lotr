package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 4
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 4
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: Ranger. Maneuver: Exert Aragorn to make him defender +1 until the regroup phase.
 */
public class Card1_089 extends AbstractCompanion {
    public Card1_089() {
        super(4, 8, 4, 6, Culture.GONDOR, Race.MAN, Signet.GANDALF, "Aragorn", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canExert(self, game, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, Filters.sameCard(self), Keyword.DEFENDER), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
