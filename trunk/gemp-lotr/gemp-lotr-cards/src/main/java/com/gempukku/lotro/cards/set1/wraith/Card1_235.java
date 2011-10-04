package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.CardAffectsCardEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 3
 * Site: 3
 * Game Text: Shadow: Exert Ulaire Otsea to make a [WRAITH] minion fierce until the regroup phase.
 */
public class Card1_235 extends AbstractMinion {
    public Card1_235() {
        super(4, 9, 3, 3, Race.NAZGUL, Culture.WRAITH, "Ulaire Otsea", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 0)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.SHADOW);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseActiveCardEffect(playerId, "Choose a WRAITH minion", Filters.culture(Culture.WRAITH), Filters.type(CardType.MINION)) {
                        @Override
                        protected void cardSelected(PhysicalCard wraithMinion) {
                            action.appendEffect(new CardAffectsCardEffect(self, wraithMinion));
                            action.appendEffect(
                                    new AddUntilStartOfPhaseModifierEffect(
                                            new KeywordModifier(self, Filters.sameCard(wraithMinion), Keyword.FIERCE), Phase.REGROUP));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
