package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.CancelEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

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
public class Card1_287 extends AbstractResponseEvent {
    public Card1_287() {
        super(Side.FREE_PEOPLE, Culture.SHIRE, "Extraordinary Resilience");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.HEAL
                && game.getModifiersQuerying().hasKeyword(game.getGameState(), ((HealCharacterEffect) effect).getCard(), Keyword.RING_BEARER)) {
            PlayEventAction action = new PlayEventAction(self);
            action.addCost(
                    new CancelEffect(effect));
            action.addEffect(
                    new RemoveBurdenEffect());
            return Collections.singletonList(action);
        }
        return null;
    }
}
