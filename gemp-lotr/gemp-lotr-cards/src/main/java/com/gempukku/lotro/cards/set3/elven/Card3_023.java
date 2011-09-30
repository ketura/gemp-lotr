package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Artifact
 * Vitality: +1
 * Game Text: Bearer must be Galadriel. At the start of each regroup phase, you may discard up to 2 cards from hand
 * to draw the same number of cards.
 */
public class Card3_023 extends AbstractAttachableFPPossession {
    public Card3_023() {
        super(0, 0, 1, Culture.ELVEN, CardType.ARTIFACT, Keyword.RING, "Nenya", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Galadriel");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.START_OF_PHASE
                && game.getGameState().getCurrentPhase() == Phase.REGROUP) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, 0, 2, Filters.any()) {
                        @Override
                        protected void cardsBeingDiscarded(Collection<PhysicalCard> cardsBeingDiscarded) {
                            int count = cardsBeingDiscarded.size();
                            if (count > 0)
                                action.appendEffect(
                                        new DrawCardEffect(playerId, count));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
