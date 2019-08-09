package com.gempukku.lotro.cards.set20.isengard;
import java.util.List;
import java.util.Collections;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.logic.modifiers.condition.InitiativeCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Isengard Unleased
 * Isengard	Condition • Support Area
 * While you have initiative, each site on the adventure path is a Battleground.
 * Response: If an Uruk-hai is about to take a wound, discard this condition to prevent that wound.
 */
public class Card20_233 extends AbstractPermanent {
    public Card20_233() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.ISENGARD, "Isengard Unleased", null, true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new KeywordModifier(self, Filters.and(CardType.SITE, Zone.ADVENTURE_PATH),
new InitiativeCondition(Side.SHADOW), Keyword.BATTLEGROUND, 1));
}

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Race.URUK_HAI)
            && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfDiscardEffect(self));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, playerId, "Choose an Uruk-hai", Race.URUK_HAI));
            return Collections.singletonList(action);
        }
        return null;
    }
}
