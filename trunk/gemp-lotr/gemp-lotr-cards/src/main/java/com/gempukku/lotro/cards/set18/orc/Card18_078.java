package com.gempukku.lotro.cards.set18.orc;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.*;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: While you can spot an [ORC] minion and fewer minions than companions, each companion loses all defender
 * bonuses and cannot gain defender bonuses. Maneuver: Spot 2 [ORC] minions to make the fellowship's current site gain
 * battleground until the regroup phase.
 */
public class Card18_078 extends AbstractPermanent {
    public Card18_078() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.ORC, Zone.SUPPORT, "Destroyers and Usurpers");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new RemoveKeywordModifier(self, CardType.COMPANION,
                new AndCondition(
                        new SpotCondition(Culture.ORC, CardType.MINION),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return Filters.countActive(gameState, modifiersQuerying, CardType.MINION)
                                        < Filters.countActive(gameState, modifiersQuerying, CardType.COMPANION);
                            }
                        }), Keyword.DEFENDER);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSpot(game, 2, Culture.ORC, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new KeywordModifier(self, game.getGameState().getCurrentSite(), Keyword.BATTLEGROUND), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
