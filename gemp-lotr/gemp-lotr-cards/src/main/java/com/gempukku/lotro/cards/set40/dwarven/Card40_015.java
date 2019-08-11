package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.effects.SelfDiscardEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: Endurance of Dwarves
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Condition - Companion
 * Vitality: +1
 * Card Number: 1U15
 * Game Text: Bearer must be a Dwarf. Skirmish: Discard this condition to make bearer strength +1.
 */
public class Card40_015 extends AbstractAttachable{
    public Card40_015(){
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.DWARVEN, null, "Endurance of Dwarves");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canSelfDiscard(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfDiscardEffect(self));
            action.appendEffect(new ChooseAndAddUntilEOPStrengthBonusEffect(
                    action, self, playerId, 1, self.getAttachedTo()));
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public int getVitality() {
        return 1;
    }
}
