package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: To play, exert an Uruk-hai. Plays to your support area. Each time a companion or ally loses a skirmish
 * involving an Uruk-hai, the opponent must choose to either exert the Ring-bearer or add a burden.
 */
public class Card1_162 extends AbstractLotroCardBlueprint {
    public Card1_162() {
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "Worry", true);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.canPayForShadowCard(game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.URUK_HAI), Filters.canExert());
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayPermanentAction action = new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose an Uruk-hai", true, Filters.keyword(Keyword.URUK_HAI), Filters.canExert()));
        return action;
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, Phase.SHADOW, self)
                && checkPlayRequirements(playerId, game, self, 0)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));
        }
        return null;
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        GameState gameState = game.getGameState();
        if (PlayConditions.winsSkirmish(gameState, game.getModifiersQuerying(), effectResult, Filters.keyword(Keyword.URUK_HAI))) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Each time a companion or ally loses a skirmish involving an Uruk-hai, the opponent must choose to either exert the Ring-bearer or add a burden.");
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(new ExertCharacterEffect(gameState.getRingBearer(gameState.getCurrentPlayerId())));
            possibleEffects.add(new AddBurdenEffect(gameState.getCurrentPlayerId()));

            action.addEffect(
                    new ChoiceEffect(action, game.getGameState().getCurrentPlayerId(), possibleEffects, false));
            return Collections.singletonList(action);
        }
        return null;
    }
}
