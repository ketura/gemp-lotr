package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.CancelEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Spell. Response: If a companion is about to take a wound, spot Gandalf to prevent that wound.
 */
public class Card1_076 extends AbstractResponseEvent {
    public Card1_076() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "Intimidate");
        addKeyword(Keyword.SPELL);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WOUND) {
            WoundResult woundResult = (WoundResult) effectResult;
            if (woundResult.getWoundedCard().getBlueprint().getCardType() == CardType.COMPANION) {
                PlayEventAction action = new PlayEventAction(self);
                action.addEffect(new CancelEffect(effect));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
