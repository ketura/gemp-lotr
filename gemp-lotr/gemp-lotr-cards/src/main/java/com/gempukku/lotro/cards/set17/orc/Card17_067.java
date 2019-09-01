package com.gempukku.lotro.cards.set17.orc;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.AssignAgainstResult;

import java.util.Arrays;
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
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.ORC, "A Defiled Charge");
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new ExertExtraPlayCostModifier(self, self, null, Culture.ORC, Race.ORC, Filters.mounted));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.assignedAgainst(game, effectResult, Side.FREE_PEOPLE, CardType.COMPANION, Culture.ORC, Race.ORC, Filters.mounted)) {
            AssignAgainstResult assignResult = (AssignAgainstResult) effectResult;
            PhysicalCard companion = assignResult.getAgainst().iterator().next();
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.setText("Assign " + GameUtils.getFullName(assignResult.getAssignedCard()) + " to " + GameUtils.getFullName(companion));
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
