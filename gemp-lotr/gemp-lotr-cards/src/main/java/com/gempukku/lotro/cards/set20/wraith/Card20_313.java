package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * You Cannot Kill Them
 * Ringwraith	Condition • Support Area
 * Response: If a Nazgul is about to take a wound, heal the Ring-bearer or remove a burden to prevent that wound.
 */
public class Card20_313 extends AbstractPermanent {
    public Card20_313() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, Zone.SUPPORT, "You Cannot Kill Them");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Race.NAZGUL)
                && (PlayConditions.canHeal(self, game, Filters.ringBearer) || PlayConditions.canRemoveBurdens(game, self, 1))) {
            ActivateCardAction action = new ActivateCardAction(self);
            List<Effect> possibleCosts = new LinkedList<Effect>();
            possibleCosts.add(
                    new ChooseAndHealCharactersEffect(action, playerId, Filters.ringBearer) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Heal the Ring-bearer";
                        }
                    });
            possibleCosts.add(
                    new RemoveBurdenEffect(playerId, self, 1));
            action.appendCost(
                    new ChoiceEffect(action, playerId, possibleCosts));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, playerId, "Choose a Nazgul to preven wound to", Race.NAZGUL));
            return Collections.singletonList(action);
        }
        return null;
    }
}
