package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Title: Alliance of Old
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition - Companion
 * Card Number: 1R93
 * Game Text: Tale. Bearer must be an Elf. Skirmish: Exert a [GONDOR] Man to make bearer strength +2.
 */
public class Card40_093 extends AbstractAttachable {
    public Card40_093() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 1, Culture.GONDOR, null, "Alliance of Old");
        addKeyword(Keyword.TALE);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.ELF;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canExert(self, game, Culture.GONDOR, Race.MAN)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GONDOR, Race.MAN));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self.getAttachedTo(), 2)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
