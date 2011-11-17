package com.gempukku.lotro.cards.set6.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Set: Ents of Fangorn
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 11
 * Vitality: 3
 * Site: 6
 * Game Text: Maneuver: Exert this minion twice to make the Free Peoples player name 3 cultures. Wound each companion
 * and ally not of a named culture.
 */
public class Card6_101 extends AbstractMinion {
    public Card6_101() {
        super(4, 11, 3, 6, Race.ORC, Culture.SAURON, "Gate Picket", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.MANEUVER, self, 0)
                && PlayConditions.canSelfExert(self, 2, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            final Set<String> possibleCultures = new LinkedHashSet<String>();
            for (Culture culture : Culture.values())
                possibleCultures.add(culture.getHumanReadable());

            action.appendEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), game.getGameState().getCurrentPlayerId(),
                            new MultipleChoiceAwaitingDecision(1, "Name first spared culture", possibleCultures.toArray(new String[possibleCultures.size()])) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    possibleCultures.add(result);
                                    final Culture firstCulture = Culture.findCultureByHumanReadable(result);
                                    action.insertEffect(
                                            new PlayoutDecisionEffect(game.getUserFeedback(), game.getGameState().getCurrentPlayerId(),
                                                    new MultipleChoiceAwaitingDecision(1, "Name second spared culture", possibleCultures.toArray(new String[possibleCultures.size()])) {
                                                        @Override
                                                        protected void validDecisionMade(int index, String result) {
                                                            possibleCultures.add(result);
                                                            final Culture secondCulture = Culture.findCultureByHumanReadable(result);
                                                            action.insertEffect(
                                                                    new PlayoutDecisionEffect(game.getUserFeedback(), game.getGameState().getCurrentPlayerId(),
                                                                            new MultipleChoiceAwaitingDecision(1, "Name third spared culture", possibleCultures.toArray(new String[possibleCultures.size()])) {
                                                                                @Override
                                                                                protected void validDecisionMade(int index, String result) {
                                                                                    Culture thirdCulture = Culture.findCultureByHumanReadable(result);

                                                                                    action.insertEffect(
                                                                                            new WoundCharactersEffect(self,
                                                                                                    Filters.and(
                                                                                                            Filters.or(CardType.COMPANION, CardType.ALLY),
                                                                                                            Filters.not(Filters.or(firstCulture, secondCulture, thirdCulture))
                                                                                                    )));
                                                                                }
                                                                            }));
                                                        }
                                                    }));
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
