package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.SkirmishResult;

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
public class Card1_020 extends AbstractLotroCardBlueprint {
    public Card1_020() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, Culture.DWARVEN, "Let Them Come!", "1_20");
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self)) {
            PlayPermanentAction action = new PlayPermanentAction(self, Zone.FREE_SUPPORT);
            return Collections.<Action>singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getRequiredWhenActions(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        EffectResult.Type resultType = effectResult.getType();
        if (resultType == EffectResult.Type.OVERWHELM_IN_SKIRMISH || resultType == EffectResult.Type.RESOLVE_SKIRMISH) {
            SkirmishResult skirmishResult = (SkirmishResult) effectResult;
            if (Filters.filter(skirmishResult.getWinners(), game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF)).size() > 0) {
                List<PhysicalCard> losingOrcs = Filters.filter(skirmishResult.getLosers(), game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ORC));

                List<Action> actions = new LinkedList<Action>();

                for (PhysicalCard losingOrc : losingOrcs) {
                    CostToEffectAction action = new CostToEffectAction(self, null, "Discard Orc");
                    action.addEffect(new DiscardCardFromPlayEffect(losingOrc));
                    actions.add(action);
                }

                return actions;
            } else if (Filters.filter(skirmishResult.getLosers(), game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.DWARF)).size() > 0) {
                CostToEffectAction action = new CostToEffectAction(self, null, "Discard this condition if Dwarf loses a skirmish");
                action.addEffect(new DiscardCardFromPlayEffect(self));
                return Collections.<Action>singletonList(action);
            }
        }
        return null;
    }
}
