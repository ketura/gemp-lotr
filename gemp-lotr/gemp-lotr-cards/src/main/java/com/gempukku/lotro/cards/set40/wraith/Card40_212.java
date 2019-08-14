package com.gempukku.lotro.cards.set40.wraith;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndReturnCardsToHandEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

import static com.gempukku.lotro.common.Race.NAZGUL;

/**
 * Title: You Cannot Kill Them
 * Set: Second Edition
 * Side: Shadow
 * Culture: Ringwraith
 * Twilight Cost: 1
 * Type: Event - Response
 * Card Number: 1U212
 * Game Text: If a Nazgul is about to be killed, return that Nazgul to your hand.
 */
public class Card40_212 extends AbstractResponseEvent {
    public Card40_212() {
        super(Side.SHADOW, 1, Culture.WRAITH, "You Cannot Kill Them");
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingKilled(effect, game, NAZGUL)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            KillEffect killEffect = (KillEffect) effect;
            PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new ChooseAndReturnCardsToHandEffect(action, playerId, 1, 1, Filters.in(killEffect.getCharactersToBeKilled()), NAZGUL));
            return Collections.singletonList(action);
        }
        return null;
    }
}
