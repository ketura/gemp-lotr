package com.gempukku.lotro.cards.set15.men;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 2
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 1
 * Site: 4
 * Game Text: Skirmish: If this minion is skirmishing, discard this minion to play a [MEN] minion from your
 * discard pile. That minion is fierce until the regroup phase.
 */
public class Card15_073 extends AbstractMinion {
    public Card15_073() {
        super(2, 8, 1, 4, Race.MAN, Culture.MEN, "Bold Easterling");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSpot(game, self, Filters.inSkirmish)
                && PlayConditions.canSelfDiscard(self, game)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.MEN, CardType.MINION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.MEN, CardType.MINION) {
                        @Override
                        protected void afterCardPlayed(PhysicalCard cardPlayed) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, cardPlayed, Keyword.FIERCE), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
