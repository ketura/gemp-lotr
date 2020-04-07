package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.modifiers.AllyParticipatesInArcheryFireModifier;
import com.gempukku.lotro.logic.modifiers.CantTakeArcheryWoundsModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. Archery: Spot 2 [ISENGARD] archers to make allies take wounds from archery
 * fire instead of companions.
 */
public class Card4_146 extends AbstractPermanent {
    public Card4_146() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.ISENGARD, "Come Down");
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.ARCHERY, self, 0)
                && PlayConditions.canSpot(game, 2, Culture.ISENGARD, Keyword.ARCHER)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new AllyParticipatesInArcheryFireModifier(self, CardType.ALLY)));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new CantTakeArcheryWoundsModifier(self, CardType.COMPANION)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
