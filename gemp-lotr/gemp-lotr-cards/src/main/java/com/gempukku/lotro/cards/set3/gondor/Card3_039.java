package com.gempukku.lotro.cards.set3.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Tale. Maneuver: Exert a [GONDOR] companion to make that companion strength +3 and defender +1 until
 * the regroup phase.
 */
public class Card3_039 extends AbstractEvent {
    public Card3_039() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Banner of the White Tree", Phase.MANEUVER);
        addKeyword(Keyword.TALE);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.GONDOR, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.GONDOR, CardType.COMPANION) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard gondorCompanion) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(gondorCompanion), 3), Phase.REGROUP));
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new KeywordModifier(self, Filters.sameCard(gondorCompanion), Keyword.DEFENDER), Phase.REGROUP));
                    }
                });
        return action;
    }
}
