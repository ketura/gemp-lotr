package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.PreventCardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
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
 * •Faramir, Noble Guide
 * Gondor	Companion • Man
 * 7	3	7
 * Ring-bound. Ranger.
 * Response: If Faramir is about to take a wound, exert a ring-bound Hobbit to prevent that wound.
 */
public class Card20_191 extends AbstractCompanion {
    public Card20_191() {
        super(3, 7, 3, 7, Culture.GONDOR, Race.MAN, null, "Faramir", "Noble Guide", true);
        addKeyword(Keyword.RING_BOUND);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, self)
                && PlayConditions.canExert(self, game, Race.HOBBIT, Keyword.RING_BOUND)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.HOBBIT, Keyword.RING_BOUND));
            action.appendEffect(
                    new PreventCardEffect((WoundCharactersEffect) effect, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
