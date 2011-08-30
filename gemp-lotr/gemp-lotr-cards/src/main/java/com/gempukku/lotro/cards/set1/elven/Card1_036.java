package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship: Exert an Elf to reveal an opponent's hand. That player discards a card from hand for each Orc
 * revealed.
 */
public class Card1_036 extends AbstractLotroCardBlueprint {
    public Card1_036() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.ELVEN, "Curse Their Foul Feet!", "1_36");
        addKeyword(Keyword.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF), Filters.canExert())) {
            final CostToEffectAction action = new CostToEffectAction(self, "Exert an Elf to reveal an opponent's hand. That player discards a card from hand for each Orc revealed.");
            action.addCost(new ChooseActiveCardEffect(playerId, "Choose an Elf", Filters.keyword(Keyword.ELF), Filters.canExert()) {
                @Override
                protected void cardSelected(LotroGame game, PhysicalCard elf) {
                    action.addCost(new ExertCharacterEffect(elf));
                    action.addEffect(new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new MultipleChoiceAwaitingDecision(1, "Choose an opponent", getOpponents(game.getGameState().getPlayerOrder().getAllPlayers(), playerId)) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    // TODO
                                }
                            })
                    );
                }
            });
            return Collections.singletonList(action);
        }
        return null;
    }

    private String[] getOpponents(List<String> players, String self) {
        List<String> shadowPlayers = new LinkedList<String>(players);
        shadowPlayers.remove(self);
        return shadowPlayers.toArray(new String[shadowPlayers.size()]);
    }
}
