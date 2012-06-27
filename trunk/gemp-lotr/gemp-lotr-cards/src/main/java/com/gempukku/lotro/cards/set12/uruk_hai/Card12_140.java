package com.gempukku.lotro.cards.set12.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterLostSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 10
 * Vitality: 3
 * Site: 5
 * Game Text: Lurker. (Skirmishes involving lurker minions must be resolved after any others.) Response: If a companion
 * loses a skirmish involving an [URUK-HAI] minion, exert this minion to discard a possession borne by that companion.
 */
public class Card12_140 extends AbstractMinion {
    public Card12_140() {
        super(4, 10, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Crushing Uruk");
        addKeyword(Keyword.LURKER);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.losesSkirmishInvolving(game, effectResult, CardType.COMPANION, Filters.and(Culture.URUK_HAI, CardType.MINION))
                && PlayConditions.canSelfExert(self, game)) {
            PhysicalCard loser = ((CharacterLostSkirmishResult) effectResult).getLoser();
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION, Filters.attachedTo(loser)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
