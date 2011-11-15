package com.gempukku.lotro.cards.set8.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.TakeControlOfASiteEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.modifiers.HasInitiativeModifier;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: While you can spot a [SAURON] minion at site 5K, the Shadow has initiative, regardless of the Free Peoples
 * player’s hand. Response: If a companion is played, discard a [SAURON] card from hand to take control of a site.
 * Discard this condition.
 */
public class Card8_104 extends AbstractPermanent {
    public Card8_104() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "Morgai", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new HasInitiativeModifier(self,
                        new AndCondition(
                                new SpotCondition(Culture.SAURON, CardType.MINION),
                                new LocationCondition(Filters.siteNumber(5), Filters.siteBlock(Block.KING))
                        ), Side.SHADOW));
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game, effectResult, CardType.COMPANION)
                && PlayConditions.canDiscardFromHand(game, playerId, 1, Culture.SAURON)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 1, Culture.SAURON));
            action.appendEffect(
                    new TakeControlOfASiteEffect(self, playerId));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
