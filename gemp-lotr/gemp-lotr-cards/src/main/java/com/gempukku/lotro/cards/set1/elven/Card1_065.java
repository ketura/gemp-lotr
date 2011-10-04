package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.AllyOnCurrentSiteModifier;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Exert an Elf ally whose home is site 6. Until the regroup phase, that ally is strength +3 and
 * participates in archery fire and skirmishes.
 */
public class Card1_065 extends AbstractEvent {
    public Card1_065() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Swan-ship of the Galadhrim", Phase.MANEUVER);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF), Filters.type(CardType.ALLY), Filters.siteNumber(6));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.ELF), Filters.type(CardType.ALLY), Filters.siteNumber(6)) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard elfAlly) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new StrengthModifier(self, Filters.sameCard(elfAlly), 3)
                                        , Phase.REGROUP));
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new AllyOnCurrentSiteModifier(self, Filters.sameCard(elfAlly))
                                        , Phase.REGROUP));
                    }
                }
        );
        return action;
    }
}
