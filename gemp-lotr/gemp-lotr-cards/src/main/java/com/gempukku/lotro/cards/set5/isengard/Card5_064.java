package com.gempukku.lotro.cards.set5.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.CancelActivatedEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.ActivateCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Possession • Mount
 * Strength: +4
 * Vitality: +2
 * Game Text: Bearer must a warg-rider. Response: If a skirmish special abiliy is used in a skirmish involving bearer,
 * exert bearer to cancel that action.
 */
public class Card5_064 extends AbstractAttachable {
    public Card5_064() {
        super(Side.SHADOW, CardType.POSSESSION, 4, Culture.ISENGARD, PossessionClass.MOUNT, "War-warg");
    }

    @Override
    public int getStrength() {
        return 4;
    }

    @Override
    public int getVitality() {
        return 2;
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.WARG_RIDER;
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.activated(game, effectResult, Filters.any)) {
            ActivateCardResult activateEffect = (ActivateCardResult) effectResult;
            if (Filters.inSkirmish.accepts(game, self.getAttachedTo())
                    && activateEffect.getActionTimeword() == Phase.SKIRMISH
                    && PlayConditions.canExert(self, game, Filters.hasAttached(self))) {
                ActivateCardAction action = new ActivateCardAction(self);
                action.appendCost(
                        new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.hasAttached(self)));
                action.appendEffect(
                        new CancelActivatedEffect(self, activateEffect));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
