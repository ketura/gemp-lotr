package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * Southron Traveler
 * Fallen Realms	Minion • Man
 * 8	1	4
 * Southron. Ambush (2).
 * Response: If a Southron is about to take a wound, remove 3 to prevent that wound
 */
public class Card20_143 extends AbstractMinion {
    public Card20_143() {
        super(3, 8, 1, 4, Race.MAN, Culture.FALLEN_REALMS, "Southron Traveler");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.AMBUSH, 2);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Keyword.SOUTHRON)
                && game.getGameState().getTwilightPool()>=3) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, playerId, "Choose Southron to prevent wound to", Keyword.SOUTHRON));
            return Collections.singletonList(action);
        }
        return null;
    }
}
