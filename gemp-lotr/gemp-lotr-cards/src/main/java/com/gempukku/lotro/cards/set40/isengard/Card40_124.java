package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: Isengard Unleashed
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Condition - Site
 * Card Number: 1R124
 * Game Text: Each site in this region is a battleground.
 * Response: If an Uruk-hai is about to take a wound, discard this condition to prevent that wound.
 */
public class Card40_124 extends AbstractAttachable {
    public Card40_124() {
        super(Side.SHADOW, CardType.CONDITION, 2, Culture.ISENGARD, null, "Isengard Unleashed");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return CardType.SITE;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        KeywordModifier modifier = new KeywordModifier(self, Filters.and(CardType.SITE, Filters.region(GameUtils.getRegion(self.getAttachedTo().getSiteNumber()))),
                Keyword.BATTLEGROUND);
        return Collections.singletonList(modifier);
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Race.URUK_HAI)
                && PlayConditions.canSelfDiscard(self, game)) {
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, woundEffect, playerId, "Choose Uruk-hai to prevent wound to", Race.URUK_HAI));
            return Collections.singletonList(action);
        }
        return null;
    }
}
