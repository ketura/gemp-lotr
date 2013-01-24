package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * Easterling Guard
 * Fallen Realms	Minion • Man
 * 9	2	4
 * Easterling.
 * Shadow: Heal this minion to add (1).
 */
public class Card20_116 extends AbstractMinion {
    public Card20_116() {
        super(4, 9, 2, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Guard");
        addKeyword(Keyword.EASTERLING);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
            && PlayConditions.canHeal(self, game, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndHealCharactersEffect(action, playerId, self));
            action.appendEffect(
                    new AddTwilightEffect(self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
