package com.gempukku.lotro.cards.set17.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.CharacterLostSkirmishResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Damage +1. Each time a minion loses a skirmish involving Thorin III, its owner discards the top card from
 * his or her draw deck.
 */
public class Card17_006 extends AbstractCompanion {
    public Card17_006() {
        super(2, 6, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Thorin III", "Stonehelm", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.losesSkirmishInvolving(game, effectResult, CardType.MINION, self)) {
            CharacterLostSkirmishResult lostSkirmish = (CharacterLostSkirmishResult) effectResult;

            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new DiscardTopCardFromDeckEffect(self, lostSkirmish.getLoser().getOwner(), 1, true));
            return Collections.singletonList(action);
        }
        return null;
    }
}
