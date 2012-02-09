package com.gempukku.lotro.cards.set17.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Hunter 1 (While skirmishing a non-hunter character, this character is strength +1.) While Orophin
 * is skirmishing a wounded minion, he is strength +3. Archery: Exert another [ELVEN] hunter and add a threat to wound
 * a minion.
 */
public class Card17_011 extends AbstractCompanion {
    public Card17_011() {
        super(2, 6, 3, 6, Culture.ELVEN, Race.ELF, null, "Orophin", true);
        addKeyword(Keyword.HUNTER, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, new SpotCondition(self, Filters.inSkirmishAgainst(CardType.MINION, Filters.wounded)), 3);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
                && PlayConditions.canExert(self, game, Filters.not(self), Culture.ELVEN, Keyword.HUNTER)
                && PlayConditions.canAddThreat(game, self, 1)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.not(self), Culture.ELVEN, Keyword.HUNTER));
            action.appendCost(
                    new AddThreatsEffect(playerId, self, 1));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
