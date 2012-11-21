package com.gempukku.lotro.cards.set12.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AbstractPreventableCardEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Response: If a companion is about to take a wound, spot a [ROHAN] companion and add 2 burdens to prevent
 * that.
 */
public class Card12_115 extends AbstractPermanent {
    public Card12_115() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Golden Glimmer");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, CardType.COMPANION)
                && PlayConditions.canSpot(game, Culture.ROHAN, CardType.COMPANION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddBurdenEffect(playerId, self, 2));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (AbstractPreventableCardEffect) effect, playerId, "Choose companion to prevent wound to", CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
