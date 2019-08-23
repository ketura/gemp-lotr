package com.gempukku.lotro.cards.set6.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAssignMinionToCompanionEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Condition
 * Strength: -1
 * Game Text: To play, spot an [ISENGARD] Man. Plays on a companion (except the Ring-bearer). Assignment: Assign
 * an [ISENGARD] minion with less strength than bearer to skirmish bearer. Discard this condition.
 */
public class Card6_061 extends AbstractAttachable {
    public Card6_061() {
        super(Side.SHADOW, CardType.CONDITION, 1, Culture.ISENGARD, null, "Desertion");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.ISENGARD, Race.MAN);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(CardType.COMPANION, Filters.not(Filters.ringBearer));
    }

    @Override
    public int getStrength() {
        return -1;
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ASSIGNMENT, self, 0)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ChooseAndAssignMinionToCompanionEffect(action, playerId, self.getAttachedTo(), Culture.ISENGARD, CardType.MINION, Filters.lessStrengthThan(self.getAttachedTo())));
            action.appendEffect(
                    new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
