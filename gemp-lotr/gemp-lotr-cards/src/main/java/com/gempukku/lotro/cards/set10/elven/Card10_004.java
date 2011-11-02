package com.gempukku.lotro.cards.set10.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Skirmish: Exert Aegnor to make a minion skirmishing an unbound [ELVEN] companion strength -1 for each archer you spot. 
 * Rarity: U
 */
public class Card10_004 extends AbstractCompanion {
    public Card10_004() {
        super(2, 6, 3, Culture.ELVEN, Race.ELF, null, "Aegnor", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.SKIRMISH, self) {
            
        	if (PlayConditions.canSelfExert(self, game)) {
        		ActivateCardAction action = new ActivateCardAction(self);
        		action.setText("Make an minion skirmishing an unbound Elf compaion strength -1 for each archer you spot");
        		action.appendCost(
        				new ExertCharactersEffect(self, self));
        		action.appendEffect(
        				int archerTotal = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ARCHER));
        				// TODO Need to make it so it targets minion skirmishing unbound elf and not just any minion
        				new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, -archerTotal, Filters.type(CardType.MINION)));
        		actions.add(action);
        	}
        }
        return null;
    }
}
