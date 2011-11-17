package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. While the fellowship is at site 6T, each Ring-bound Man takes no more than
 * 1 wound during each skirmish phase. Fellowship: Add (2) and discard this condition to heal a Ring-bound Man.
 */
public class Card4_120 extends AbstractPermanent {
    public Card4_120() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.GONDOR, Zone.SUPPORT, "Forbidden Pool", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new CantTakeMoreThanXWoundsModifier(self, Phase.SKIRMISH, 1,
                        new LocationCondition(Filters.siteNumber(6), Filters.siteBlock(Block.TWO_TOWERS)),
                        Race.MAN, Keyword.RING_BOUND));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddTwilightEffect(self, 2));
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, Race.MAN, Keyword.RING_BOUND));
            return Collections.singletonList(action);
        }
        return null;
    }
}
