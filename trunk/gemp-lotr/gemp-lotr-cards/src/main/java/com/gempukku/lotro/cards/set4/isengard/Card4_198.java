package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. Each time a companion or ally is killed during a skirmish involving an Uruk-hai, you may take
 * control of a site.
 */
public class Card4_198 extends AbstractMinion {
    public Card4_198() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Stormer");
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.KILL
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.inSkirmish, Race.URUK_HAI)) {
            KillResult killResult = (KillResult) effectResult;
            final Collection<PhysicalCard> killedFPs = Filters.filter(killResult.getKilledCards(), game.getGameState(), game.getModifiersQuerying(), Filters.or(CardType.ALLY, CardType.COMPANION));
            List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();
            for (int i = 0; i < killedFPs.size(); i++) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.appendEffect(
                        new TakeControlOfASiteEffect(self, playerId));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
