package com.gempukku.lotro.cards.set32.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Artifact • Ring
 * Vitality: +1
 * Game Text: Bearer must be a [DWARVEN] companion. Bearer is strength +1 for each [DWARVEN] possession and
 * [DWARVEN] artifact in your discard pile (limit +3). Maneuver: Take a [DWARVEN] event into hand from your
 * discard pile. Discard this artifact.
 */
public class Card32_007 extends AbstractAttachableFPPossession {
    public Card32_007() {
        super(0, 0, 1, Culture.DWARVEN, CardType.ARTIFACT, PossessionClass.RING, "Ring of Thror", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.DWARVEN, CardType.COMPANION);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(new StrengthModifier(self, Filters.hasAttached(self), null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                        return Filters.filter(game.getGameState().getDiscard(self.getOwner()), game, Culture.DWARVEN, Filters.or(CardType.POSSESSION, CardType.ARTIFACT)).size();
                    }
                }));
    }

    @Override
    public List<ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndPutCardFromDiscardIntoHandEffect(action, playerId, 1, 1, Culture.DWARVEN, CardType.EVENT));
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
