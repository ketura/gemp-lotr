package com.gempukku.lotro.cards.set17.men;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.StackCardFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndTransferAttachableEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be a [MEN] minion. Each time bearer wins a skirmish you may transfer this possession
 * to another [MEN] minion. If you do, stack a [MEN] minion from play on a [MEN] possession in your support area.
 */
public class Card17_047 extends AbstractAttachable {
    public Card17_047() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.MEN, PossessionClass.HAND_WEAPON, "Primitive Brand");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.MEN, CardType.MINION);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.hasAttached(self), 2);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self.getAttachedTo())) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndTransferAttachableEffect(action, playerId, self, Filters.any, Filters.any) {
                        @Override
                        protected void afterTransferCallback(PhysicalCard transferredCard, PhysicalCard transferredFrom, PhysicalCard transferredTo) {
                            action.appendEffect(
                                    new ChooseActiveCardEffect(self, playerId, "Choose a MEN minion to stack", Culture.MEN, CardType.MINION) {
                                        @Override
                                        protected void cardSelected(LotroGame game, final PhysicalCard minion) {
                                            action.appendEffect(
                                                    new ChooseActiveCardEffect(self, playerId, "Choose a MEN possession in your support area", Culture.MEN, CardType.POSSESSION, Zone.SUPPORT) {
                                                        @Override
                                                        protected void cardSelected(LotroGame game, PhysicalCard possession) {
                                                            action.appendEffect(
                                                                    new StackCardFromPlayEffect(minion, possession));
                                                        }
                                                    });
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
