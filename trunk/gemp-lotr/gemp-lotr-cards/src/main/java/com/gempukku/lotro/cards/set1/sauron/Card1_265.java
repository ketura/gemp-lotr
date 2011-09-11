package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If a companion is killed by a [SAURON] Orc, the Free Peoples player must discard 3 cards from
 * the top of his draw deck for each card in the dead pile.
 */
public class Card1_265 extends AbstractResponseEvent {
    public Card1_265() {
        super(Side.SHADOW, Culture.SAURON, "Orc Butchery");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, (Phase) null, self)
                && PlayConditions.canPayForShadowCard(game, self, 0)) {
            Skirmish skirmish = game.getGameState().getSkirmish();
            if (effectResult.getType() == EffectResult.Type.KILL
                    && ((KillResult) effectResult).getKilledCard().getBlueprint().getCardType() == CardType.COMPANION
                    && skirmish != null
                    && Filters.filter(skirmish.getShadowCharacters(), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.race(Race.ORC)).size() > 0) {
                PlayEventAction action = new PlayEventAction(self);
                int deadPileCount = game.getGameState().getDeadPile(game.getGameState().getCurrentPlayerId()).size();
                for (int i = 0; i < deadPileCount * 3; i++)
                    action.addEffect(new DiscardTopCardFromDeckEffect(game.getGameState().getCurrentPlayerId()));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
