package com.gempukku.lotro.cards.set2.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ExertCharactersCost;
import com.gempukku.lotro.cards.effects.CancelEventEffect;
import com.gempukku.lotro.cards.modifiers.RoamingPenaltyModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.PlayEventEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

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
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new RoamingPenaltyModifier(self, Filters.and(Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION)), -1));
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effect, Filters.type(CardType.EVENT))
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            PlayEventEffect playEffect = (PlayEventEffect) effect;
            if (playEffect.isRequiresRanger()) {
                ActivateCardAction action = new ActivateCardAction(self, Keyword.RESPONSE);
                action.appendCost(
                        new ExertCharactersCost(playerId, self));
                action.appendEffect(
                        new CancelEventEffect(playerId, playEffect));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
