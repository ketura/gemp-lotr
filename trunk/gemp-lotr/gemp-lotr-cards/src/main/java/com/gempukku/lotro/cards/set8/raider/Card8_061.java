package com.gempukku.lotro.cards.set8.raider;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.modifiers.HasInitiativeModifier;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: While you can spot a [RAIDER] minion at site 4K, the Shadow has initiative, regardless of the Free Peoples
 * player’s hand. Response: If a companion is played, discard a [RAIDER] card from hand to add (5).
 * Discard this condition.
 */
public class Card8_061 extends AbstractPermanent {
    public Card8_061() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.RAIDER, Zone.SUPPORT, "Haradwaith", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new HasInitiativeModifier(self,
                        new AndCondition(
                                new LocationCondition(Filters.siteNumber(4), Filters.siteBlock(Block.KING)),
                                new SpotCondition(Culture.RAIDER, CardType.MINION)
                        ), Side.SHADOW));
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, CardType.COMPANION)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Culture.RAIDER)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.RAIDER));
            action.appendEffect(
                    new AddTwilightEffect(self, 5));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
