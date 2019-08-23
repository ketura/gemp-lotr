package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Legolas, Swiftshot
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion - Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 8
 * Card Number: 1C52
 * Game Text: Archer. Archery: If Legolas is at a battleground, exert him to add 1 to the fellowship archery total.
 */
public class Card40_052 extends AbstractCompanion{
    public Card40_052() {
        super(2, 6, 3, 8, Culture.ELVEN, Race.ELF, null, "Legolas", "Swiftshot", true);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.ARCHERY, self)
                && PlayConditions.location(game, Keyword.BATTLEGROUND)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new ArcheryTotalModifier(self, Side.FREE_PEOPLE, 1)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
