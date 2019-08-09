package com.gempukku.lotro.cards.set31.elven;

import com.gempukku.lotro.logic.cardtype.AbstractAlly;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.AllyParticipatesInArcheryFireAndSkirmishesModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Ally • Home 5 • Elf
 * Strength: 7
 * Vitality: 4
 * Site: 5
 * Game Text: Archer. Maneuver: If you can spot more minions than companions, exert Elf Army to allow it
 * to participate in archery fire and skirmishes until the regroup phase.
 */
public class Card31_008 extends AbstractAlly {
    public Card31_008() {
        super(3, Block.HOBBIT, 5, 7, 4, Race.ELF, Culture.ELVEN, "Elf Army", null, true);
		addKeyword(Keyword.ARCHER);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canSelfExert(self, game)
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.MINION) > Filters.countActive(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfExertEffect(action, self));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new AllyParticipatesInArcheryFireAndSkirmishesModifier(self, self), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
	}
}
