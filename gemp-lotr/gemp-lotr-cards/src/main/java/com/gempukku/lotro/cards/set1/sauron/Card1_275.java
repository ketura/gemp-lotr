package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CancelEffect;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Search. Plays to your support area. Response: If a stealth event is played, exert a [SAURON] tracker
 * to cancel that event.
 */
public class Card1_275 extends AbstractPermanent {
    public Card1_275() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, Zone.SHADOW_SUPPORT, "Seeking It Always");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), null, self, 0)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.keyword(Keyword.TRACKER), Filters.canExert())
                && PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effect, Filters.and(Filters.type(CardType.EVENT), Filters.keyword(Keyword.STEALTH)))) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Exert a SAURON tracker to cancel that event.");
            action.addCost(
                    new ChooseAndExertCharacterEffect(action, playerId, "Choose a SAURON tracker", true, Filters.culture(Culture.SAURON), Filters.keyword(Keyword.TRACKER), Filters.canExert()));
            action.addEffect(
                    new CancelEffect(playerId, effect));
            return Collections.singletonList(action);
        }
        return null;
    }
}
