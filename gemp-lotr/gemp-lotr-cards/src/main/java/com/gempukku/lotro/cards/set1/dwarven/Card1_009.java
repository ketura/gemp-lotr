package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Possession � Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be a Dwarf. Each time a player's minion loses a skirmish to bearer, that player discards the
 * top card from his draw deck.
 */
public class Card1_009 extends AbstractAttachableFPPossession {
    public Card1_009() {
        super(0, 2, 0, Culture.DWARVEN, PossessionClass.HAND_WEAPON, "Dwarven Axe");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.DWARF;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(effectResult, self.getAttachedTo())) {
            SkirmishResult skirmishResult = ((SkirmishResult) effectResult);
            List<PhysicalCard> losers = skirmishResult.getLosers();

            List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();

            Set<String> losingPlayers = new HashSet<String>();
            for (PhysicalCard loser : losers)
                losingPlayers.add(loser.getOwner());

            for (String losingPlayer : losingPlayers) {
                RequiredTriggerAction action = new RequiredTriggerAction(self);
                action.appendEffect(new DiscardTopCardFromDeckEffect(self, losingPlayer, true));
                actions.add(action);
            }

            return actions;
        }

        return null;
    }
}
