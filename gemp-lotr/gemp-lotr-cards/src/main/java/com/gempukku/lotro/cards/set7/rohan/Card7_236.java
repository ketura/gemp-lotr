package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.PreventCardEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Vitality: +1
 * Game Text: Bearer must be Theoden. He is damage +1. Response: If Theoden is about to take a wound and you cannot spot
 * 3 threats, add 2 threats to prevent that wound.
 */
public class Card7_236 extends AbstractAttachableFPPossession {
    public Card7_236() {
        super(3, 2, 1, Culture.ROHAN, PossessionClass.HAND_WEAPON, "Herugrim", "Sword of the Mark", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name(Names.theoden);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE, 1));
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Filters.name(Names.theoden))
                && !PlayConditions.canSpotThreat(game, 3)
                && PlayConditions.canAddThreat(game, self, 2)) {

            PhysicalCard theoden = Filters.findFirstActive(game, Filters.name(Names.theoden));
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;

            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new AddThreatsEffect(playerId, self, 2));
            action.appendEffect(
                    new PreventCardEffect(woundEffect, theoden));
            return Collections.singletonList(action);
        }
        return null;
    }
}
