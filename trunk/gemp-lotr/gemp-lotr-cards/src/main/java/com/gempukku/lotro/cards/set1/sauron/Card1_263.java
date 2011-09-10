package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Each time a companion or ally loses a skirmish that involves a [SAURON] Orc,
 * each [SAURON] Orc is strength +1 until the regroup phase.
 */
public class Card1_263 extends AbstractLotroCardBlueprint {
    public Card1_263() {
        super(Side.SHADOW, CardType.CONDITION, Culture.SAURON, "Orc Banner");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.canPayForShadowCard(game, self, twilightModifier);
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return new PlayPermanentAction(self, Zone.SHADOW_SUPPORT, twilightModifier);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.losesSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.type(CardType.ALLY), Filters.type(CardType.COMPANION)))
                && PlayConditions.winsSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.culture(Culture.SAURON), Filters.keyword(Keyword.ORC)))) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Each [SAURON] Orc is strength +1 until the regroup phase.");
            action.addEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new StrengthModifier(self, Filters.and(Filters.culture(Culture.SAURON), Filters.keyword(Keyword.ORC)), 1), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
