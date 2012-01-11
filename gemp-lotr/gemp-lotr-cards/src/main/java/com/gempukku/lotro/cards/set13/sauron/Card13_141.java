package com.gempukku.lotro.cards.set13.sauron;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Artifact • Hand Weapon
 * Strength: +2
 * Vitality: +1
 * Game Text: Bearer must be Sauron. He is damage +1. Each time Sauron wins a skirmish and the companion he
 * is skirmishing is not killed, you may assign X wounds to other companions, where X is the current region number.
 */
public class Card13_141 extends AbstractAttachable {
    public Card13_141() {
        super(Side.SHADOW, CardType.ARTIFACT, 0, Culture.SAURON, PossessionClass.HAND_WEAPON, "Sceptre of the Dark Lord", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Sauron");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE, 1));

        return modifiers;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))
                && PlayConditions.canSpot(game, CardType.COMPANION, Filters.inSkirmish)) {
            int region = GameUtils.getRegion(game.getGameState());
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            for (int i = 0; i < region; i++)
                action.appendEffect(
                        new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.COMPANION, Filters.not(Filters.inSkirmish)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
