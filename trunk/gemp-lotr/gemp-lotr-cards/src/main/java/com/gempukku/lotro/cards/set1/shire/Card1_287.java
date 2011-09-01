package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.CancelEffect;
import com.gempukku.lotro.cards.effects.RemoveBurderEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.HealResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If the Ring-bearer is about to heal, remove a burden instead.
 */
public class Card1_287 extends AbstractLotroCardBlueprint {
    public Card1_287() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.SHIRE, "Extraordinary Resilience");
        addKeyword(Keyword.RESPONSE);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.HEAL
                && game.getModifiersQuerying().hasKeyword(game.getGameState(), ((HealResult) effectResult).getCard(), Keyword.RING_BEARER)) {
            PlayEventAction action = new PlayEventAction(self);
            action.addCost(
                    new CancelEffect(effect));
            action.addEffect(
                    new RemoveBurderEffect(playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
