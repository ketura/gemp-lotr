package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.CancelEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Search. Response: If a stealth event is played, spot 3 [MORIA] minions to cancel that event.
 */
public class Card1_194 extends AbstractResponseEvent {
    public Card1_194() {
        super(Side.SHADOW, Culture.MORIA, "Relentless");
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.and(Filters.type(CardType.EVENT), Filters.keyword(Keyword.STEALTH)))
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), Filters.type(CardType.MINION)) >= 3
                && PlayConditions.canPayForShadowCard(game, self, 0)) {
            PlayEventAction action = new PlayEventAction(self);
            action.addEffect(new CancelEffect(effect));
            return Collections.singletonList(action);
        }
        return null;
    }
}
