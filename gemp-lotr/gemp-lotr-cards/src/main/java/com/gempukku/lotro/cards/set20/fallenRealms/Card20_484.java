package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * ❶ Serrated Axe [Fal]
 * Possession • Hand Weapon
 * Strength: +2
 * Bearer must be an Easterling.
 * Each time bearer wins a skirmish, you may heal a companion to exert another companion (except the Ring-bearer).
 * <p/>
 * http://lotrtcg.org/coreset/fallenrealms/serratedaxe(r3).jpg
 */
public class Card20_484 extends AbstractAttachable {
    public Card20_484() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.FALLEN_REALMS, PossessionClass.HAND_WEAPON, "Serrated Axe");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.EASTERLING;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, Filters.hasAttached(self), 2));
}

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))
                && PlayConditions.canHeal(self, game, CardType.COMPANION)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION) {
                        @Override
                        protected void forEachCardChosenToHealCallback(PhysicalCard character) {
                            action.appendEffect(
                                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.not(character), CardType.COMPANION, Filters.not(Filters.ringBearer)));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
