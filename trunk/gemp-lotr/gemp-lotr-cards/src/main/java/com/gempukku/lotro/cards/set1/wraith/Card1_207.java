package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CancelEventEffect;
import com.gempukku.lotro.cards.effects.TransferPermanentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.PlayEventEffect;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Skirmish: Transfer this condition from your support area to a character
 * skirmishing a Nazgul. Burdens and wounds may not be removed from bearer.
 */
public class Card1_207 extends AbstractPermanent {
    public Card1_207() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.WRAITH, Zone.SHADOW_SUPPORT, "Black Breath");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && self.getZone() == Zone.SHADOW_SUPPORT
                && Filters.filter(game.getGameState().getSkirmish().getShadowCharacters(), game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.NAZGUL)).size() > 0
                && game.getGameState().getSkirmish().getFellowshipCharacter() != null) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.SKIRMISH);
            action.appendEffect(
                    new TransferPermanentEffect(self, game.getGameState().getSkirmish().getFellowshipCharacter()));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new AbstractModifier(self, "Burdens and wounds may not be removed from bearer.", Filters.hasAttached(self), new ModifierEffect[]{ModifierEffect.WOUND_MODIFIER}) {
            @Override
            public boolean canBeHealed(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard card, boolean result) {
                return false;
            }
        };
    }

    @Override
    public List<RequiredTriggerAction> getRequiredBeforeTriggers(LotroGame game, Effect effect, PhysicalCard self) {
        if (effect.getType() == EffectResult.Type.REMOVE_BURDEN
                && self.getZone() == Zone.ATTACHED) {
            if (game.getModifiersQuerying().hasKeyword(game.getGameState(), self.getStackedOn(), Keyword.RING_BEARER)) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new CancelEventEffect(self.getOwner(), (PlayEventEffect) effect));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
