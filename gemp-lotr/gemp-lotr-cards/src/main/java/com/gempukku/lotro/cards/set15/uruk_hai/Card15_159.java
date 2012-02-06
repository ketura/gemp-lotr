package com.gempukku.lotro.cards.set15.uruk_hai;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.modifiers.CantReplaceSiteByFPPlayerModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.EffectResult;

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
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.URUK_HAI, Zone.SUPPORT, "Defensive Rush");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantReplaceSiteByFPPlayerModifier(self, new SpotCondition(Culture.URUK_HAI, CardType.MINION), Filters.any);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && game.getGameState().getTwilightPool() >= 3) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            boolean controlsSite = PlayConditions.controllsSite(game, playerId);
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, controlsSite ? 2 : 1, CardType.MINION, Culture.URUK_HAI));
            return Collections.singletonList(action);
        }
        return null;
    }
}
