package com.gempukku.lotro.cards.set13.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 6
 * Type: Minion • Troll
 * Strength: 12
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. Fierce. To play, spot an [ORC] minion. Shadow: Discard an [ORC] condition from play to play
 * this minion from your discard pile.
 */
public class Card13_117 extends AbstractMinion {
    public Card13_117() {
        super(6, 12, 3, 5, Race.TROLL, Culture.ORC, "Ordnance Grunt");
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.ORC, CardType.MINION);
    }

    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.isPhase(game, Phase.SHADOW)
                && PlayConditions.canDiscardFromPlay(self, game, Culture.ORC, CardType.CONDITION)
                && PlayConditions.canPlayFromDiscard(playerId, game, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Culture.ORC, CardType.CONDITION));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
