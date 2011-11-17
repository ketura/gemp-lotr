package com.gempukku.lotro.cards.set6.isengard;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be an [ISENGARD] minion. Each time bearer wins a skirmish, the Free Peoples player must exert
 * a companion for each site you control.
 */
public class Card6_059 extends AbstractAttachable {
    public Card6_059() {
        super(Side.SHADOW, CardType.POSSESSION, 0, Culture.ISENGARD, PossessionClass.HAND_WEAPON, "Banner of Isengard");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.ISENGARD, CardType.MINION);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.hasAttached(self), 1));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self.getAttachedTo())) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int sitesControlled = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlled(self.getOwner()));
            for (int i = 0; i < sitesControlled; i++)
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
