package com.gempukku.lotro.cards.set5.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. The twilight cost of the first [SAURON] Orc played at site 4T is -3.
 * Shadow: Spot 2 [SAURON] Orcs and discard 4 cards from hand to draw 3 cards. Discard this condition.
 */
public class Card5_102 extends AbstractPermanent {
    public Card5_102() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "Morannon", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new TwilightCostModifier(self,
                Filters.and(
                        Culture.SAURON,
                        Race.ORC,
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return self.getData() == null;
                            }
                        }),
                new LocationCondition(Filters.siteNumber(4), Filters.siteBlock(Block.TWO_TOWERS)), -3);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, Culture.SAURON, Race.ORC)
                && Filters.and(Filters.siteNumber(4), Filters.siteBlock(Block.TWO_TOWERS)).accepts(game.getGameState(), game.getModifiersQuerying(), game.getGameState().getCurrentSite()))
            self.storeData(new Object());
        if (effectResult.getType() == EffectResult.Type.END_OF_TURN
                && self.getData() != null)
            self.removeData();
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canSpot(game, 2, Culture.SAURON, Race.ORC)
                && game.getGameState().getHand(playerId).size() >= 4) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 4));
            action.appendEffect(
                    new DrawCardEffect(playerId, 3));
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
