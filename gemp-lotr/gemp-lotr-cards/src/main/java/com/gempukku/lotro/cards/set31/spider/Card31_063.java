package com.gempukku.lotro.cards.set31.spider;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Spider
 * Twilight Cost: 2
 * Type: Minion • Spider
 * Strength: 7
 * Vitality: 2
 * Site: 5
 * Game Text: Fierce. Skirmish: Discard an Orc to make a Spider strength +3 (or +5 if you can spot 6 companions)
 * until the regroup phase.
 */
public class Card31_063 extends AbstractMinion {
    public Card31_063() {
        super(2, 7, 2, 5, Race.SPIDER, Culture.GUNDABAD, "Wicked Spider");
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canDiscardFromPlay(self, game, Race.ORC)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Race.ORC));
            action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose a Spider", Race.SPIDER) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard spider) {
                        if (PlayConditions.canSpot(game, 6, CardType.COMPANION)) {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(spider), 5), Phase.REGROUP));
						} else {
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new StrengthModifier(self, Filters.sameCard(spider), 3), Phase.REGROUP));
                        }
                    }
			});
            return Collections.singletonList(action);
        }
        return null;
    }
}
