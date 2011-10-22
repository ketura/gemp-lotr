package com.gempukku.lotro.cards.set6.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. At sites 2T and 3T, The Balrog's twilight cost is -3. Skirmish: Discard this
 * condition to make a unique [MORIA] minion strength +3.
 */
public class Card6_077 extends AbstractPermanent {
    public Card6_077() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.MORIA, Zone.SUPPORT, "Durin's Tower", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new TwilightCostModifier(self, Filters.name("The Balrog"), new LocationCondition(Filters.or(Filters.siteNumber(2), Filters.siteNumber(3)), Filters.siteBlock(Block.TWO_TOWERS)), -3));
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new DiscardCardsFromPlayEffect(self, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 3, Culture.MORIA, CardType.MINION, Filters.unique));
            return Collections.singletonList(action);
        }
        return null;
    }
}
