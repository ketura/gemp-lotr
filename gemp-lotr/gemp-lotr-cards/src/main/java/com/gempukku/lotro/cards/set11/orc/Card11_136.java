package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 6
 * Vitality: 2
 * Site: 4
 * Game Text: Lurker. (Skirmishes involving lurker minions must be resolved after any others.) Skirmish: Remove (2)
 * to make an [ORC] minion strength +1.
 */
public class Card11_136 extends AbstractMinion {
    public Card11_136() {
        super(3, 6, 2, 4, Race.ORC, Culture.ORC, "Prowling Orc");
        addKeyword(Keyword.LURKER);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 2)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(2));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 1, Culture.ORC, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
