package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: To play, spot an Uruk-hai. Plays to your support area. Response: If your Uruk-hai wins a skirmish, remove
 * (3) to make him fierce until the regroup phase.
 */
public class Card1_159 extends AbstractLotroCardBlueprint {
    public Card1_159() {
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "Uruk-hai Rampage", "1_159");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.URUK_HAI))) {
            PlayPermanentAction action = new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getPlayableWhenActions(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.owner(playerId), Filters.keyword(Keyword.URUK_HAI)))
                && game.getGameState().getTwilightPool() >= 3) {
            SkirmishResult skirmishResult = ((SkirmishResult) effectResult);

            final CostToEffectAction action = new CostToEffectAction(self, Keyword.RESPONSE, "If your Uruk-hai wins a skirmish, remove (3) to make him fierce until the regroup phase.");

            action.addCost(new RemoveTwilightEffect(3));
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose a winning Uruk-hai", Filters.and(Filters.owner(playerId), Filters.keyword(Keyword.URUK_HAI), Filters.in(skirmishResult.getWinners()))) {
                        @Override
                        protected void cardSelected(PhysicalCard winningUrukHai) {
                            action.addEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, Filters.sameCard(winningUrukHai), Keyword.FIERCE), Phase.REGROUP));
                        }
                    });

            return Collections.singletonList(action);
        }
        return null;
    }
}
