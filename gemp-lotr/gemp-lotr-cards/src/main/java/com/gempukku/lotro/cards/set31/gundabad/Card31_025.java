package com.gempukku.lotro.cards.set31.gundabad;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.VitalityModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Gundabad
 * Twilight Cost: 3
 * Type: Possession • Mount
 * Strength: +2
 * Vitality: +1
 * Game Text: Bearer must an Orc. Bearer is fierce. Each time Bearer wins a skirmish, the Free Peoples player
 * must exert a [DWARVEN] companion for each 2 [GANDALF] cards you spot.
 */
public class Card31_025 extends AbstractAttachable {
    public Card31_025() {
        super(Side.SHADOW, CardType.POSSESSION, 3, Culture.GUNDABAD, PossessionClass.MOUNT, "Savage Warg");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.FIERCE));
        return modifiers;
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.ORC;
    }
	
	
    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self.getAttachedTo())) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            int gandalfCardsSpotted = Filters.countActive(game, Culture.GANDALF);
            for (int i = 1; i < gandalfCardsSpotted; i += 2)
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, Race.DWARF, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
}
}
