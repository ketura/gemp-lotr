package com.gempukku.lotro.cards.set2.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 8
 * Type: Minion • Nazgul
 * Strength: 14
 * Vitality: 4
 * Site: 3
 * Game Text: Twilight. Return to Its Master may not be played. Each time The Witch-king wins a skirmish, you may exert
 * him to wound the Ring-bearer twice.
 */
public class Card2_085 extends AbstractMinion {
    public Card2_085() {
        super(8, 14, 4, 3, Race.NAZGUL, Culture.WRAITH, "The Witch-king", true);
        addKeyword(Keyword.TWILIGHT);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new AbstractModifier(self, "Return to Its Master may not be played", Filters.name("Return to Its Master"), ModifierEffect.ACTION_MODIFIER) {
                    @Override
                    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, String performingPlayer, Action action) {
                        PhysicalCard actionSource = action.getActionSource();
                        if (actionSource != null && actionSource.getBlueprint().getName().equals("Return to Its Master"))
                            return false;
                        return true;
                    }
                });
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(effectResult, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new WoundCharactersEffect(self, Filters.keyword(Keyword.RING_BEARER)));
            action.appendEffect(
                    new WoundCharactersEffect(self, Filters.keyword(Keyword.RING_BEARER)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
