package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.SelfDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterWonSkirmishResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each time a Dwarf wins a skirmish against an Orc, discard that Orc. Discard
 * this condition if a Dwarf loses a skirmish.
 */
public class Card1_020 extends AbstractPermanent {
    public Card1_020() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Let Them Come!");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmishInvolving(game, effectResult, Race.DWARF, Race.ORC)) {
            CharacterWonSkirmishResult wonResult = (CharacterWonSkirmishResult) effectResult;
            List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();
            for (PhysicalCard losingOrc : Filters.filter(wonResult.getInvolving(), game.getGameState(), game.getModifiersQuerying(), Race.ORC)) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.setText("Discard " + GameUtils.getCardLink(losingOrc));
                action.appendEffect(new DiscardCardsFromPlayEffect(self, losingOrc));
                actions.add(action);
            }
            return actions;
        }
        if (TriggerConditions.losesSkirmish(game, effectResult, Race.DWARF)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(new SelfDiscardEffect(self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
