package com.gempukku.lotro.cards.set40.isengard;

import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.PreventCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Title: *Saruman's Staff, Instrument of War
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Artifact - Staff
 * Strength: +2
 * Vitality: +1
 * Card Number: 1R137
 * Game Text: Bearer must be Saruman. He loses cunning and is fierce.
 * Response: If Saruman is about to take a wound, remove a threat to prevent that wound.
 */
public class Card40_137 extends AbstractAttachable {
    public Card40_137() {
        super(Side.SHADOW, CardType.ARTIFACT, 2, Culture.ISENGARD, PossessionClass.STAFF, "Saruman's Staff", "Instrument of War", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.saruman;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        RemoveKeywordModifier losesCunning = new RemoveKeywordModifier(self, Filters.hasAttached(self), Keyword.CUNNING);
        KeywordModifier fierce = new KeywordModifier(self, Filters.hasAttached(self), Keyword.FIERCE);
        return Arrays.asList(losesCunning, fierce);
    }

    @Override
    public List<? extends Action> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Filters.hasAttached(self))
                && PlayConditions.canRemoveThreat(game, self, 1)) {
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new PreventCardEffect(woundEffect, self.getAttachedTo()));
            return Collections.singletonList(action);
        }
        return null;
    }
}
