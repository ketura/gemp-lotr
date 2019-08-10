package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.effects.CancelSkirmishEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Robin Smallburrow, Shiriff of Hobbiton
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Ally - Hobbit - Shire
 * Strength: 2
 * Vitality: 2
 * Card Number: 1R265
 * Game Text: To play, spot 2 Hobbit companions.
 * Skirmish: At sites 1-4, exert Robin Smallburrow to cancel a skirmish involving a Hobbit.
 * Skirmish: At sites 5-9, exert Robin Smallburrow to make a Hobbit strength +3.
 */
public class Card40_265 extends AbstractAlly {
    public Card40_265() {
        super(1, SitesBlock.SECOND_ED, 0, 2, 2, Race.HOBBIT, Culture.SHIRE, "Robin Smallburrow",
                "Sheriff of Hobbiton", true);
        addKeyword(Keyword.SHIRE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, Race.HOBBIT, CardType.COMPANION);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfExertEffect(action, self));
            if (game.getGameState().getCurrentSiteNumber() <= 4) {
                action.appendEffect(
                        new CancelSkirmishEffect(Race.HOBBIT));
            } else {
                action.appendEffect(
                        new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 3, Race.HOBBIT));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
