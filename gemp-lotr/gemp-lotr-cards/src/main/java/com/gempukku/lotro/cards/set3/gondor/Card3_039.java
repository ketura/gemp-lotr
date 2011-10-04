package com.gempukku.lotro.cards.set3.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

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
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Banner of the White Tree", Phase.MANEUVER);
        addKeyword(Keyword.TALE);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.GONDOR), Filters.type(CardType.COMPANION));
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.culture(Culture.GONDOR), Filters.type(CardType.COMPANION)) {
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
