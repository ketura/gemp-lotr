package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AbstractPreventableCardEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 3
 * Site: 4
 * Game Text: Lurker. (Skirmishes involving lurker minions must be resolved after any others.) Response: If a [MEN]
 * minion is about to take a wound in a skirmish, exert this minion and remove (1) to prevent that.
 */
public class Card11_076 extends AbstractMinion {
    public Card11_076() {
        super(4, 9, 3, 4, Race.MAN, Culture.MEN, "Easterling Shield Wall");
        addKeyword(Keyword.LURKER);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Culture.MEN, CardType.MINION)
                && PlayConditions.isPhase(game, Phase.SKIRMISH)
                && PlayConditions.canSelfExert(self, game)
                && game.getGameState().getTwilightPool() >= 1) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(1));
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (AbstractPreventableCardEffect) effect, playerId, "Choose a minion", Culture.MEN, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
