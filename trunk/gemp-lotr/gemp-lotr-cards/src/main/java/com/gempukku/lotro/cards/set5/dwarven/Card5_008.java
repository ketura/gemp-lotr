package com.gempukku.lotro.cards.set5.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.RemoveKeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Battle of Helm's Deep
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Possession
 * Game Text: Plays to your support area. Maneuver: Exert a Dwarf companion to make each minion at a battleground lose
 * all damage bonuses until the regroup phase.
 */
public class Card5_008 extends AbstractPermanent {
    public Card5_008() {
        super(Side.FREE_PEOPLE, 2, CardType.POSSESSION, Culture.DWARVEN, Zone.SUPPORT, "Horn of Helm", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canExert(self, game, Race.DWARF, CardType.COMPANION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.DWARF, CardType.COMPANION));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new RemoveKeywordModifier(self, CardType.MINION, new LocationCondition(Keyword.BATTLEGROUND), Keyword.DAMAGE), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
