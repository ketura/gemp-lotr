package com.gempukku.lotro.cards.set13.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveFromTheGameCardsInPlayEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

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
 * Game Text: Denethor is resistance +1 for each [GONDOR] possession in your discard pile. Maneuver: Spot a [GONDOR]
 * possession borne by a companion and remove that possession from the game to exert a minion twice.
 */
public class Card13_064 extends AbstractCompanion {
    public Card13_064() {
        super(3, 7, 3, 5, Culture.GONDOR, Race.MAN, null, "Denethor", "Last Ruling Steward", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new ResistanceModifier(self, self,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                        return Filters.filter(gameState.getDiscard(self.getOwner()), gameState, modifiersQuerying, Culture.GONDOR, CardType.POSSESSION).size();
                    }
                });
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSpot(game, Culture.GONDOR, CardType.POSSESSION, Filters.attachedTo(CardType.COMPANION))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndRemoveFromTheGameCardsInPlayEffect(action, playerId, 1, 1, Culture.GONDOR, CardType.POSSESSION, Filters.attachedTo(CardType.COMPANION)));
            action.appendEffect(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
