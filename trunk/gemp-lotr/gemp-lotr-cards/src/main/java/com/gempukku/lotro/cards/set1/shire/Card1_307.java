package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
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
        super(1, 3, 4, Culture.SHIRE, Race.HOBBIT, Signet.GANDALF, "Pippin", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);

            List<Effect> possibleEffects = new LinkedList<Effect>();
            possibleEffects.add(new RemoveBurdenEffect(self) {
                @Override
                public String getText(LotroGame game) {
                    return "Remove a burden";
                }
            });
            possibleEffects.add(
                    new ChooseAndHealCharactersEffect(action, self.getOwner(), CardType.COMPANION) {
                        @Override
                        public String getText(LotroGame game) {
                            return "Heal a companion";
                        }
                    });
            action.appendEffect(new ChoiceEffect(action, self.getOwner(), possibleEffects));
            return Collections.singletonList(action);
        }
        return null;
    }
}
