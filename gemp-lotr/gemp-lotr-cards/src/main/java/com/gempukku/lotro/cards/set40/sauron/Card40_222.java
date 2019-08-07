package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.CantTakeWoundsModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Mordor Archer Captain
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 7
 * Type: Minion - Orc
 * Strength: 13
 * Vitality: 4
 * Home: 6
 * Card Number: 1R222
 * Game Text: Archer. Mordor Archer Captain's twilight cost is -1 for each [SAURON] archer you can spot.
 * Archery: Exert Mordor Archer Captain twice and spot an unbound companion. That companion may not take wounds during the archery phase.
 */
public class Card40_222 extends AbstractMinion {
    public Card40_222() {
        super(7, 13, 4, 6, Race.ORC, Culture.SAURON, "Mordor Archer Captain", null, true);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return -Filters.countActive(gameState, modifiersQuerying, Culture.SAURON, Keyword.ARCHER);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ARCHERY, self, 0)
                && PlayConditions.canSelfExert(self, 2, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose unbound companion", Filters.unboundCompanion) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                            action.appendEffect(
                                    new AddUntilEndOfPhaseModifierEffect(
                                            new CantTakeWoundsModifier(self, card)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
