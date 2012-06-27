package com.gempukku.lotro.cards.set19.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PreventableEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.actions.SubAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ages End
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 7
 * Type: Minion • Troll
 * Strength: 13
 * Vitality: 4
 * Site: 4
 * Game Text: Hunter 2. (While skirmishing a non-hunter character, this character is strength +2.)
 * To play, spot an [ORC] Orc. When you play this minion, add 2 threats. The Free Peoples player may exert
 * the Ring-bearer to prevent this.
 */
public class Card19_022 extends AbstractMinion {
    public Card19_022() {
        super(7, 13, 4, 4, Race.TROLL, Culture.ORC, "Pit Troll");
        addKeyword(Keyword.HUNTER, 2);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ORC, Race.ORC);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new PreventableEffect(action,
                            new AddThreatsEffect(self.getOwner(), self, 2), game.getGameState().getCurrentPlayerId(),
                            new PreventableEffect.PreventionCost() {
                                @Override
                                public Effect createPreventionCostForPlayer(SubAction subAction, String playerId) {
                                    final PhysicalCard ringBearer = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Filters.ringBearer);
                                    return new ExertCharactersEffect(action, self, ringBearer);
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
