package com.gempukku.lotro.cards.set6.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ents of Fangorn
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Skirmish: Exert an Elf to make a minion skirmishing Arwen strength -1.
 */
public class Card6_013 extends AbstractCompanion {
    public Card6_013() {
        super(2, 6, 3, Culture.ELVEN, Race.ELF, Signet.ARAGORN, "Arwen", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, Race.ELF)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.ELF));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -1, CardType.MINION, Filters.inSkirmishAgainst(self)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
