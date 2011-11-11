package com.gempukku.lotro.cards.set8.gollum;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 4
 * Type: Condition • Support Area
 * Game Text: Each time a companion takes a wound during a skirmish that involves a [GOLLUM] minion, exert a companion.
 */
public class Card8_024 extends AbstractPermanent {
    public Card8_024() {
        super(Side.SHADOW, 4, CardType.CONDITION, Culture.GOLLUM, Zone.SUPPORT, "Promise Keeping", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WOUND
                && PlayConditions.canSpot(game, Culture.GOLLUM, CardType.MINION, Filters.inSkirmish)) {
            WoundResult woundResult = (WoundResult) effectResult;
            List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();
            for (PhysicalCard physicalCard : Filters.filter(woundResult.getWoundedCards(), game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION)) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(
                        new ChooseAndExertCharactersEffect(action, self.getOwner(), 1, 1, CardType.COMPANION));
                actions.add(action);
            }
            return actions;
        }
        return null;
    }
}
