package com.gempukku.lotro.cards.set5.shire;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.CancelActivatedEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ActivateCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition
 * Vitality: +1
 * Game Text: Plays on Sam. Response: If a regroup special ability is used, discard Smeagol or Gollum to cancel
 * that action.
 */
public class Card5_114 extends AbstractAttachable {
    public Card5_114() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.SHIRE, null, "Rare Good Balast", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.sam;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
    }

    @Override
    public List<? extends Action> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.activated(game, effectResult, Filters.any)) {
            ActivateCardResult activateEffect = (ActivateCardResult) effectResult;
            if (activateEffect.getActionTimeword() == Phase.REGROUP
                    && PlayConditions.canDiscardFromPlay(self, game, Filters.gollumOrSmeagol)) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.gollumOrSmeagol));
                action.appendEffect(
                        new CancelActivatedEffect(self, activateEffect));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
