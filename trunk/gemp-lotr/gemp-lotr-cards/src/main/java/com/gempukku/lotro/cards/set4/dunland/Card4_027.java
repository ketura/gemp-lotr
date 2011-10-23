package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. Each time a [DUNLAND] Man wins a skirmish involving a [ROHAN] Man, you may
 * make that minion strength +2 and fierce until the regroup phase.
 */
public class Card4_027 extends AbstractPermanent {
    public Card4_027() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.DUNLAND, Zone.SUPPORT, "Living Off Rock");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.losesSkirmish(game.getGameState(), game.getModifiersQuerying(), effectResult,
                Filters.and(Filters.culture(Culture.ROHAN), Filters.race(Race.MAN)))) {

            SkirmishResult skirmishResult = (SkirmishResult) effectResult;
            List<PhysicalCard> winners = skirmishResult.getWinners();
            Collection<PhysicalCard> winningDunlandMan = Filters.filter(winners, game.getGameState(), game.getModifiersQuerying(), Filters.and(Filters.culture(Culture.DUNLAND), Filters.race(Race.MAN)));

            List<OptionalTriggerAction> actions = new LinkedList<OptionalTriggerAction>();
            for (final PhysicalCard physicalCard : winningDunlandMan) {
                OptionalTriggerAction action = new OptionalTriggerAction(self);
                action.setText("Make " + GameUtils.getCardLink(physicalCard) + " strength +2 and Fierce");
                action.appendEffect(
                        new AddUntilStartOfPhaseModifierEffect(
                                new StrengthModifier(self, Filters.sameCard(physicalCard), 2), Phase.REGROUP));
                action.appendEffect(
                        new AddUntilStartOfPhaseModifierEffect(
                                new KeywordModifier(self, Filters.sameCard(physicalCard), Keyword.FIERCE), Phase.REGROUP));
                actions.add(action);
            }

            return actions;
        }
        return null;
    }
}
