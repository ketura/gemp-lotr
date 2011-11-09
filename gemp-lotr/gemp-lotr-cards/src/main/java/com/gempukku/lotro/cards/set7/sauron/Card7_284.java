package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AssignmentEffect;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.effects.ThreatWoundsEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.results.KillResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 6
 * Game Text: Response: If a companion is overwhelmed during a skirmish involving a [SAURON] Orc and threat wounds are
 * about to be placed, remove a threat to assign this minion to the Ring-bearer (even if the Ring-bearer is already
 * assigned).
 */
public class Card7_284 extends AbstractMinion {
    public Card7_284() {
        super(3, 9, 3, 6, Race.ORC, Culture.SAURON, "Mordor Assassin");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == Effect.Type.BEFORE_THREAT_WOUNDS
                && PlayConditions.canRemoveThreat(game, self, 1)) {
            ThreatWoundsEffect threatWoundsEffect = (ThreatWoundsEffect) effect;
            KillResult killResult = threatWoundsEffect.getKillResult();
            if (killResult.getCause() == KillEffect.Cause.OVERWHELM
                    && Filters.filter(killResult.getKilledCards(), game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION).size() > 0
                    && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.inSkirmish, Culture.SAURON, Race.ORC)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new RemoveThreatsEffect(self, 1));
                PhysicalCard ringBearer = Filters.findFirstActive(game.getGameState(), game.getModifiersQuerying(), Keyword.RING_BEARER);
                AssignmentEffect assignmentEffect = new AssignmentEffect(playerId, ringBearer, self);
                assignmentEffect.setIgnoreSingleMinionRestriction(true);
                action.appendEffect(assignmentEffect);
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
