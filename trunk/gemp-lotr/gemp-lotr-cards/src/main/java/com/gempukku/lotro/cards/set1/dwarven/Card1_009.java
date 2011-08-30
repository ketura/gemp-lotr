package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.timing.Action;
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
        super(0, Culture.DWARVEN, "Dwarven Axe", "1_9");
        addKeyword(Keyword.HAND_WEAPON);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        List<Action> actions = new LinkedList<Action>();

        Filter validTargetFilter = Filters.and(Filters.keyword(Keyword.DWARF), Filters.not(Filters.attached(Filters.keyword(Keyword.HAND_WEAPON))));

        appendAttachCardAction(actions, game, self, validTargetFilter);
        appendTransferPossessionAction(actions, game, self, validTargetFilter);

        return actions;
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new StrengthModifier(self, Filters.attachedTo(self), 2);
    }

    @Override
    public List<? extends Action> getRequiredWhenActions(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.winsSkirmish(effectResult, self.getAttachedTo())) {
            SkirmishResult skirmishResult = ((SkirmishResult) effectResult);
            List<PhysicalCard> losers = skirmishResult.getLosers();

            List<Action> actions = new LinkedList<Action>();

            Set<String> losingPlayers = new HashSet<String>();
            for (PhysicalCard loser : losers)
                losingPlayers.add(loser.getOwner());

            for (String losingPlayer : losingPlayers) {
                CostToEffectAction action = new CostToEffectAction(self, "Discard top card of deck belonging to  " + losingPlayer);
                action.addEffect(new DiscardTopCardFromDeckEffect(losingPlayer));
                actions.add(action);
            }

            return actions;
        }

        return null;
    }
}
