package com.gempukku.lotro.cards.set19.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.PreventCardEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AbstractPreventableCardEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Ages End
 * Side: Shadow
 * Culture: Uruk-Hai
 * Twilight Cost: 7
 * Type: Minion • Uruk-Hai
 * Strength: 13
 * Vitality: 3
 * Site: 5
 * Game Text: Archer. Damage +1. Response: If Lurtz is about to take a wound or be exerted, remove (2) (or exert another
 * Uruk-hai) to prevent that wound or exertion.
 */
public class Card19_033 extends AbstractMinion {
    public Card19_033() {
        super(7, 13, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Lurtz", "Resilient Captain", true);
        addKeyword(Keyword.ARCHER);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if ((TriggerConditions.isGettingWounded(effect, game, self) || TriggerConditions.isGettingWounded(effect, game, self))
                && (game.getGameState().getTwilightPool() >= 2 || PlayConditions.canExert(self, game, Filters.not(self), Race.URUK_HAI))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new RemoveTwilightEffect(2));
            possibleCosts.add(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.not(self), Race.URUK_HAI) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert another Uruk-hai";
                        }
                    });
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new PreventCardEffect((AbstractPreventableCardEffect) effect, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
