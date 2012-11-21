package com.gempukku.lotro.cards.set17.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ReturnCardsToHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Artifact • Ring
 * Strength: +1
 * Game Text: Bearer must be a Nazgul. Each time bearer wins a skirmish, the Free Peoples player must either add
 * a burden or exert a companion. Response: If a player reconciles, return bearer to his owner's hand.
 */
public class Card17_142 extends AbstractAttachable {
    public Card17_142() {
        super(Side.SHADOW, CardType.ARTIFACT, 0, Culture.WRAITH, PossessionClass.RING, "Ring of Savagery", null, true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.NAZGUL;
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.hasAttached(self), 1);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.hasAttached(self))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new AddBurdenEffect(game.getGameState().getCurrentPlayerId(), self, 1));
            possibleEffects.add(
                    new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Exert a companion";
                        }
                    });
            action.appendEffect(
                    new ChoiceEffect(action, game.getGameState().getCurrentPlayerId(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.RECONCILE) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendEffect(
                    new ReturnCardsToHandEffect(self, self.getAttachedTo()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
