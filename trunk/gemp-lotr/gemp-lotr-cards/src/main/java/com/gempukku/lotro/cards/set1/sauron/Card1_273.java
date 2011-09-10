package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Response: If a [SAURON] Orc kills a companion, add 1 burden (or 3 if the Ring-bearer wears The One Ring).
 */
public class Card1_273 extends AbstractResponseEvent {
    public Card1_273() {
        super(Side.SHADOW, Culture.SAURON, "The Ring's Oppression");
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }

    @Override
    public List<? extends Action> getOptionalAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.canPlayCardDuringPhase(game, (Phase) null, self)
                && PlayConditions.canPayForShadowCard(game, self, 0)) {
            Skirmish skirmish = game.getGameState().getSkirmish();
            if (effectResult.getType() == EffectResult.Type.KILL
                    && ((KillResult) effectResult).getKilledCard().getBlueprint().getCardType() == CardType.COMPANION
                    && skirmish != null
                    && Filters.filter(skirmish.getShadowCharacters(), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.keyword(Keyword.ORC)).size() > 0) {
                PlayEventAction action = new PlayEventAction(self);
                int burdens = (game.getGameState().isWearingRing()) ? 3 : 1;
                for (int i = 0; i < burdens; i++)
                    action.addEffect(
                            new AddBurdenEffect(game.getGameState().getCurrentPlayerId()));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
