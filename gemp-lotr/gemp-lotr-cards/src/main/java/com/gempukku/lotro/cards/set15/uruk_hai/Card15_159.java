package com.gempukku.lotro.cards.set15.uruk_hai;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.modifiers.CantReplaceSiteByFPPlayerModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: While you can spot an [URUK-HAI] minion, the Free Peoples player cannot replace a site. At the start
 * of each skirmish, you may remove (3) to heal an [URUK-HAI] minion (or heal an [URUK-HAI] minion twice if you
 * control a site.)
 */
public class Card15_159 extends AbstractPermanent {
    public Card15_159() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.URUK_HAI, "Defensive Rush");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new CantReplaceSiteByFPPlayerModifier(self, new SpotCondition(Culture.URUK_HAI, CardType.MINION), Filters.any));
}

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && game.getGameState().getTwilightPool() >= 3) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            boolean controlsSite = PlayConditions.controlsSite(game, playerId);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, controlsSite ? 2 : 1, CardType.MINION, Culture.URUK_HAI));
            return Collections.singletonList(action);
        }
        return null;
    }
}
