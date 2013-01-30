package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 0
 * Goblin Armory
 * Moria	Condition • Support Area
 * Each time you play a [Moria] weapon, add (1).
 * Response: If a [Moria] Goblin is about to take a wound, discard this condition to prevent that wound.
 */
public class Card20_259 extends AbstractPermanent {
    public Card20_259() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.MORIA, Zone.SUPPORT, "Goblin Armory");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Filters.and(Filters.owner(self.getOwner()), Culture.MORIA, Filters.weapon))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(new AddTwilightEffect(self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, final Effect effect, final PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Culture.MORIA, Race.GOBLIN)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, playerId, "Choose MORIA Goblin", Culture.MORIA, Race.GOBLIN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
