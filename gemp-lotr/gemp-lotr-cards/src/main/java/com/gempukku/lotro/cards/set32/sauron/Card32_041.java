package com.gempukku.lotro.cards.set32.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Possession • Ranged Weapon
 * Strength: +1
 * Game Text: Bearer must be a [SAURON] Orc. Bearer is an archer. When you play this possession, you may
 * discard a [DWARVEN] weapon. The Free Peoples player may exert its bearer to prevent this.
 */
public class Card32_041 extends AbstractAttachable {
    public Card32_041() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.SAURON, PossessionClass.RANGED_WEAPON, "Orkish Bow");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.SAURON, Race.ORC);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.ARCHER));
        return modifiers;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseActiveCardEffect(self, playerId, "Choose DWARVEN weapon", Culture.DWARVEN, Filters.weapon) {
                        @Override
                        public void cardSelected(final LotroGame game, final PhysicalCard weapon) {
                            action.appendEffect(
                                    new PreventableEffect(action,
                                            new UnrespondableEffect() {
                                                @Override
                                                public String getText(LotroGame game) {
                                                    return "Discard " + weapon.getBlueprint().getTitle();
                                                }

                                                @Override
                                                public void doPlayEffect(LotroGame game) {
                                                    action.appendEffect(
                                                            new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.sameCard(weapon)));
                                                }
                                            },
                                            Collections.singletonList(game.getGameState().getCurrentPlayerId()),
                                            new PreventableEffect.PreventionCost() {
                                                @Override
                                                public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                                    return new ChooseAndExertCharactersEffect(subAction, playerId, 1, 1, Filters.hasAttached(Filters.sameCard(weapon))) {
                                                        @Override
                                                        public String getText(LotroGame game) {
                                                            return "Exert bearer";
                                                        }
                                                    };
                                                }
                                            }));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
