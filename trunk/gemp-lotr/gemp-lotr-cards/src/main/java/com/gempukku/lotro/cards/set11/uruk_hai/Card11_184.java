package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.NegateWoundEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 5
 * Type: Minion • Uruk-Hai
 * Strength: 13
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. Each time this minion is about to take a wound at a battleground site, you may spot another
 * [URUK-HAI] card to make the Free Peoples player draw a card instead.
 */
public class Card11_184 extends AbstractMinion {
    public Card11_184() {
        super(5, 13, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Force of Uruk-hai");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalBeforeTriggers(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, self)
                && PlayConditions.location(game, Keyword.BATTLEGROUND)
                && PlayConditions.canSpot(game, Filters.not(self), Culture.URUK_HAI)) {
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;

            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new NegateWoundEffect(woundEffect, self));
            action.appendEffect(
                    new DrawCardsEffect(game.getGameState().getCurrentPlayerId(), 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
