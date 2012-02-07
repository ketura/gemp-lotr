package com.gempukku.lotro.cards.set15.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. At the start of the maneuver phase, you may spot another [URUK-HAI] minion
 * and remove 3 [URUK-HAI] tokens to take control of a site or discard a follower.
 */
public class Card15_176 extends AbstractMinion {
    public Card15_176() {
        super(3, 8, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk Village Assassin");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)
                && PlayConditions.canSpot(game, Filters.not(self), Culture.URUK_HAI, CardType.MINION)
                && PlayConditions.canRemoveTokens(game, Token.URUK_HAI, 3, Filters.any)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.URUK_HAI, 3, Filters.any));
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new TakeControlOfASiteEffect(self, playerId));
            possibleEffects.add(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.FOLLOWER) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Discard a follower";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, playerId, possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
