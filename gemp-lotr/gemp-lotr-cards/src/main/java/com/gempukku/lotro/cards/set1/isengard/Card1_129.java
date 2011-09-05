package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: To play, exert an Uruk-hai. Plays to your support area. The twilight cost of each companion played to a site which is not a sanctuary is +2.
 */
public class Card1_129 extends AbstractLotroCardBlueprint {
    public Card1_129() {
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "The Misadventure of Mr. Underhill");
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.URUK_HAI), Filters.canExert());
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayPermanentAction action = new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
        action.addCost(
                new ChooseActiveCardEffect(playerId, "Choose an Uruk-hai", Filters.keyword(Keyword.URUK_HAI), Filters.canExert()) {
                    @Override
                    protected void cardSelected(PhysicalCard urukHai) {
                        action.addCost(new ExertCharacterEffect(urukHai));
                    }
                }
        );
        return action;
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)
                && checkPlayRequirements(playerId, game, self)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));
        }
        return null;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new TwilightCostModifier(self,
                Filters.and(
                        Filters.type(CardType.COMPANION),
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return !modifiersQuerying.hasKeyword(gameState, gameState.getCurrentSite(), Keyword.SANCTUARY);
                            }
                        }
                ), 2);
    }
}
