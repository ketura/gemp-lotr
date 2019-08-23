package com.gempukku.lotro.cards.set9.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ResistanceModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Artifact • Support Area
 * Game Text: Tale.To play, spot a [GONDOR] Man. The Ring-bearer is resistance +1 for each [GONDOR] artifact you can
 * spot (limit +3). Fellowship: Discard this artifact to play a ring from your discard pile.
 */
public class Card9_036 extends AbstractPermanent {
    public Card9_036() {
        super(Side.FREE_PEOPLE, 1, CardType.ARTIFACT, Culture.GONDOR, "Scroll of Isildur", null, true);
        addKeyword(Keyword.TALE);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.GONDOR, Race.MAN);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ResistanceModifier(self, Filters.ringBearer, new CountActiveEvaluator(3, Culture.GONDOR, CardType.ARTIFACT)));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && PlayConditions.canSelfDiscard(self, game)
                && PlayConditions.canPlayFromDiscard(playerId, game, PossessionClass.RING)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, PossessionClass.RING));
            return Collections.singletonList(action);
        }
        return null;
    }
}
