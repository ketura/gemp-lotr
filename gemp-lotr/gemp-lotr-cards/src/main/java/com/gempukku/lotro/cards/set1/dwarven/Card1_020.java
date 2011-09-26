package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;

import java.util.Collection;
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
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, Zone.FREE_SUPPORT, "Let Them Come!");
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        EffectResult.Type resultType = effectResult.getType();
        if (resultType == EffectResult.Type.OVERWHELM_IN_SKIRMISH || resultType == EffectResult.Type.RESOLVE_SKIRMISH) {
            SkirmishResult skirmishResult = (SkirmishResult) effectResult;
            if (Filters.filter(skirmishResult.getWinners(), game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF)).size() > 0) {
                Collection<PhysicalCard> losingOrcs = Filters.filter(skirmishResult.getLosers(), game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ORC));

                List<RequiredTriggerAction> actions = new LinkedList<RequiredTriggerAction>();

                for (PhysicalCard losingOrc : losingOrcs) {
                    RequiredTriggerAction action = new RequiredTriggerAction(self, null, "Discard Orc");
                    action.appendEffect(new CardAffectsCardEffect(self, losingOrc));
                    action.appendEffect(new DiscardCardsFromPlayEffect(self, losingOrc));
                    actions.add(action);
                }

                return actions;
            } else if (Filters.filter(skirmishResult.getLosers(), game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.DWARF)).size() > 0) {
                RequiredTriggerAction action = new RequiredTriggerAction(self, null, "Discard this condition if Dwarf loses a skirmish");
                action.appendEffect(new DiscardCardsFromPlayEffect(self, self));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
