package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.state.Skirmish;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 5
 * Type: Minion • Nazgul
 * Strength: 10
 * Vitality: 3
 * Site: 3
 * Game Text: Fierce. Skirmish: Exert Ulaire Cantea to discard a weapon borne by a character he is skirmishing.
 */
public class Card1_230 extends AbstractMinion {
    public Card1_230() {
        super(5, 10, 3, 3, Race.NAZGUL, Culture.WRAITH, "Ulaire Cantea", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self, 0)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.SKIRMISH, "Exert Ulaire Cantea to discard a weapon borne by a character he is skirmishing.");
            action.addCost(
                    new ExertCharacterEffect(playerId, self));
            Skirmish skirmish = game.getGameState().getSkirmish();
            if (skirmish != null && skirmish.getShadowCharacters().contains(self)) {
                action.addEffect(
                        new ChooseActiveCardEffect(playerId, "Choose a weapon borne by a character he is skirmishing", Filters.or(Filters.keyword(Keyword.HAND_WEAPON), Filters.keyword(Keyword.RANGED_WEAPON)), Filters.attachedTo(Filters.sameCard(skirmish.getFellowshipCharacter()))) {
                            @Override
                            protected void cardSelected(PhysicalCard weapon) {
                                action.addEffect(new CardAffectsCardEffect(self, weapon));
                                action.addEffect(
                                        new DiscardCardFromPlayEffect(self, weapon));
                            }
                        });
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
