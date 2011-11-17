package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: To play, exert an Uruk-hai. Plays to your support area. The twilight cost of each companion played to a site which is not a sanctuary is +2.
 */
public class Card1_129 extends AbstractPermanent {
    public Card1_129() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "The Misadventure of Mr. Underhill");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.URUK_HAI));
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayPermanentAction action = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.URUK_HAI)));
        return action;
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new TwilightCostModifier(self,
                Filters.and(
                        CardType.COMPANION,
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return !modifiersQuerying.hasKeyword(gameState, gameState.getCurrentSite(), Keyword.SANCTUARY);
                            }
                        }
                ), 2);
    }
}
