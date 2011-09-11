package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Possession
 * Game Text: Pipeweed. Plays to your support area. When you play this possession, you may draw a card.
 */
public class Card1_305 extends AbstractPermanent {
    public Card1_305() {
        super(Side.FREE_PEOPLE, 1, CardType.POSSESSION, Culture.SHIRE, Zone.FREE_SUPPORT, "Old Tobby");
        addKeyword(Keyword.PIPEWEED);
    }

    @Override
    public List<? extends Action> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Draw a card");
            action.addEffect(
                    new DrawCardEffect(playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
