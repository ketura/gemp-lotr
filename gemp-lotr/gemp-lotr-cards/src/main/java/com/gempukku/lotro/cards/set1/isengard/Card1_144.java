package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
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
public class Card1_144 extends AbstractPermanent {
    public Card1_144() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.ISENGARD, Zone.SHADOW_SUPPORT, "Uruk Bloodlust");
    }

    @Override
    public List<? extends Action> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.keyword(Keyword.URUK_HAI), Filters.owner(playerId)))
                && game.getGameState().getTwilightPool() >= 1) {
            SkirmishResult skirmishResult = ((SkirmishResult) effectResult);
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.RESPONSE, "If your Uruk-hai wins a skirmish, remove (1) to heal him.");
            action.addCost(new RemoveTwilightEffect(1));
            action.addEffect(
                    new ChooseActiveCardEffect(playerId, "Choose Uruk-hai to heal", Filters.keyword(Keyword.URUK_HAI), Filters.in(skirmishResult.getWinners())) {
                        @Override
                        protected void cardSelected(PhysicalCard winningUrukHai) {
                            action.addEffect(new HealCharacterEffect(winningUrukHai));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
