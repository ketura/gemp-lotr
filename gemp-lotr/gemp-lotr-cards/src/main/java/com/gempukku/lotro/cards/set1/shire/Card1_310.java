package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.MakeRingBearerEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 6
 * Signet: Frodo
 * Game Text: Fellowship: Play Bill the Pony from your draw deck. Response: If Frodo dies, make Sam the Ring-bearer
 * (resistance 5).
 */
public class Card1_310 extends AbstractCompanion {
    public Card1_310() {
        super(2, 3, 4, Culture.SHIRE, "Sam", true);
        addKeyword(Keyword.HOBBIT);
        setSignet(Signet.FRODO);
    }

    @Override
    public int getResistance() {
        return 5;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)) {
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Play Bill the Pony from your draw deck.");
            action.addEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose Bill the Pony to play", game.getGameState().getDeck(playerId),
                            Filters.and(Filters.name("Bill the Pony"), Filters.playable(game)), 0, 1) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard selectedCard = selectedCards.get(0);
                                game.getActionsEnvironment().addActionToStack(selectedCard.getBlueprint().getPlayCardAction(playerId, game, selectedCard, 0));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }

    @Override
    public List<? extends Action> getOptionalBeforeTriggers(String playerId, LotroGame lotroGame, Effect effect, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.KILL) {
            KillResult killResult = (KillResult) effectResult;
            if (killResult.getKilledCard().getBlueprint().getName().equals("Frodo")) {
                DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.RESPONSE, "Make Sam the Ring-Bearer");
                action.addEffect(new MakeRingBearerEffect(self));
                return Collections.singletonList(action);
            }
        }
        return null;
    }
}
