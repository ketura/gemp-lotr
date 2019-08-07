package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Title: Orc Bowcaster
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Minion - Orc
 * Strength: 5
 * Vitality: 1
 * Home: 6
 * Card Number: 1C225
 * Game Text: Archer. To play, spot a [SAURON] minion.
 * Response: If this minion is about to be killed during the archery phase, discard it to heal another [SAURON] archer.
 */
public class Card40_225 extends AbstractMinion {
    public Card40_225() {
        super(1, 5, 1, 6, Race.ORC, Culture.SAURON, "Orc Bowcaster");
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.SAURON, CardType.MINION);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingKilled(effect, game, self)
                && PlayConditions.isPhase(game, Phase.ARCHERY)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Culture.SAURON, Keyword.ARCHER, Filters.not(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
