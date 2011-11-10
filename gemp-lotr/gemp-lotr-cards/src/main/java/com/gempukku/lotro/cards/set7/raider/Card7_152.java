package com.gempukku.lotro.cards.set7.raider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 8
 * Type: Minion • Man
 * Strength: 16
 * Vitality: 4
 * Site: 4
 * Game Text: Southron. To play, spot a [RAIDER] Man. While you can spot 6 threats, each time this minion wins
 * a skirmish, the companion he was skirmishing is killed.
 */
public class Card7_152 extends AbstractMinion {
    public Card7_152() {
        super(8, 16, 4, 4, Race.MAN, Culture.RAIDER, "Mumak Commander");
        addKeyword(Keyword.SOUTHRON);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Culture.RAIDER, Race.MAN);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmishAgainst(game, effectResult, self, CardType.COMPANION)
                && game.getGameState().getThreats() >= 6) {
            SkirmishResult skirmishResult = (SkirmishResult) effectResult;
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new KillEffect(skirmishResult.getLosers(), KillEffect.Cause.CARD_EFFECT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
