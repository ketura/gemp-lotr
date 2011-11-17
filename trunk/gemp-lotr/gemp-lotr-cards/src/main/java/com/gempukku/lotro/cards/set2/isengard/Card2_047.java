package com.gempukku.lotro.cards.set2.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.CancelEventEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.RoamingPenaltyModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.PlayEventResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 7
 * Vitality: 2
 * Site: 5
 * Game Text: Tracker. Damage +1. The roaming penalty for each [ISENGARD] minion you play is -1. Response: If an event
 * is played that spots or exerts a ranger, exert this minion to cancel that event.
 */
public class Card2_047 extends AbstractMinion {
    public Card2_047() {
        super(3, 7, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Scout");
        addKeyword(Keyword.TRACKER);
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new RoamingPenaltyModifier(self, Filters.and(Culture.ISENGARD, CardType.MINION), -1));
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, CardType.EVENT)
                && PlayConditions.canExert(self, game, self)) {
            PlayEventResult playEffect = (PlayEventResult) effectResult;
            if (playEffect.isRequiresRanger()) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new ExertCharactersEffect(self, self));
                action.appendEffect(
                        new CancelEventEffect(self, playEffect));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
