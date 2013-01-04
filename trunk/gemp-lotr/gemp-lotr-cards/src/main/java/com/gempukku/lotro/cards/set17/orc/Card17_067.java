package com.gempukku.lotro.cards.set17.orc;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.AssignAgainstResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Condition • Support Area
 * Game Text: To play, exert your mounted [ORC] Orc. Each time the Free Peoples player assigns a mounted [ORC] Orc to
 * a companion, he or she must choose to either exert that companion or add (2).
 */
public class Card17_067 extends AbstractPermanent {
    public Card17_067() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.ORC, Zone.SUPPORT, "A Defiled Charge");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, Filters.owner(playerId), Culture.ORC, Race.ORC, Filters.mounted);
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayPermanentAction permanentAction = super.getPlayCardAction(playerId, game, self, twilightModifier, ignoreRoamingPenalty);
        permanentAction.appendCost(
                new ChooseAndExertCharactersEffect(permanentAction, playerId, 1, 1, Filters.owner(playerId), Culture.ORC, Race.ORC, Filters.mounted));
        return permanentAction;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.assignedAgainst(game, effectResult, Side.FREE_PEOPLE, CardType.COMPANION, Culture.ORC, Race.ORC, Filters.mounted)) {
            AssignAgainstResult assignResult = (AssignAgainstResult) effectResult;
            PhysicalCard companion = assignResult.getAgainst().iterator().next();
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.setText("Assigned " + GameUtils.getCardLink(assignResult.getAssignedCard()) + " to " + GameUtils.getCardLink(companion));
            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(
                    new ExertCharactersEffect(action, self, companion));
            possibleEffects.add(
                    new AddTwilightEffect(self, 2));
            action.appendEffect(
                    new ChoiceEffect(action, game.getGameState().getCurrentPlayerId(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
