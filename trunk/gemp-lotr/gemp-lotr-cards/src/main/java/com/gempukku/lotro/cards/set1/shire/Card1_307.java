package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.HealCharacterEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: When you play Pippin, remove a burden or wound from a companion.
 */
public class Card1_307 extends AbstractCompanion {
    public Card1_307() {
        super(1, 3, 4, Culture.SHIRE, Signet.GANDALF, "Pippin", true);
        addKeyword(Keyword.HOBBIT);
    }

    @Override
    public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Remove a burder or wound from a companion");

            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(new RemoveBurdenEffect(self.getOwner()));
            possibleEffects.add(
                    new ChooseActiveCardEffect(self.getOwner(), "Choose a companion", Filters.type(CardType.COMPANION)) {
                        @Override
                        public String getText() {
                            return "Remove a wound form a companion";
                        }

                        @Override
                        protected void cardSelected(PhysicalCard card) {
                            action.addEffect(new HealCharacterEffect(card));
                        }
                    });

            action.addEffect(new ChoiceEffect(action, self.getOwner(), possibleEffects, false));
            return Collections.singletonList(action);
        }
        return null;
    }
}
