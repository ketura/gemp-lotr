package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. While the fellowship is at site 7T, each Ring-bound Man is defender +1.
 * Skirmish: Spot a Ringbound Man and discard this condition to wound an opponent's Man.
 */
public class Card4_133 extends AbstractPermanent {
    public Card4_133() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Ruins of Osgiliath", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.and(Filters.race(Race.MAN), Filters.keyword(Keyword.RING_BOUND)),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                return gameState.getCurrentSiteNumber() == 7 && gameState.getCurrentSiteBlock() == Block.TWO_TOWERS;
                            }
                        }, Keyword.DEFENDER, 1));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.MAN), Filters.keyword(Keyword.RING_BOUND))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.not(Filters.owner(playerId)), Filters.race(Race.MAN)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
