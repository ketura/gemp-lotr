package com.gempukku.lotro.cards.set13.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 5
 * Game Text: Knight. Skirmish: Discard a [GONDOR] condition from your support area to make this companion strength +2.
 */
public class Card13_067 extends AbstractCompanion {
    public Card13_067() {
        super(3, 7, 3, 5, Culture.GONDOR, Race.MAN, null, "Guard of the White Tree");
        addKeyword(Keyword.KNIGHT);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromPlay(self, game, Culture.GONDOR, CardType.CONDITION, Zone.SUPPORT, Filters.owner(playerId))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.GONDOR, CardType.CONDITION, Zone.SUPPORT, Filters.owner(playerId)));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self, 2), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
