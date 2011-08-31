package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.HealCharacterEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Condition
 * Game Text: Plays to your support area. Response: If your Uruk-hai wins a skirmish, remove (1) to heal him.
 */
public class Card1_144 extends AbstractLotroCardBlueprint {
    public Card1_144() {
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "Uruk Bloodlust", "1_144");
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)) {
            PlayPermanentAction action = new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
            return Collections.singletonList(action);
        }

        return null;
    }

    @Override
    public List<? extends Action> getPlayableWhenActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.keyword(Keyword.URUK_HAI), Filters.owner(playerId)))
                && game.getGameState().getTwilightPool() >= 1) {
            SkirmishResult skirmishResult = ((SkirmishResult) effectResult);
            final CostToEffectAction action = new CostToEffectAction(self, "If your Uruk-hai wins a skirmish, remove (1) to heal him.");
            action.addCost(new RemoveTwilightEffect(1));
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose Uruk-hai to heal", Filters.keyword(Keyword.URUK_HAI), Filters.in(skirmishResult.getWinners())) {
                        @Override
                        protected void cardSelected(LotroGame game, PhysicalCard winningUrukHai) {
                            action.addEffect(new HealCharacterEffect(winningUrukHai));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
