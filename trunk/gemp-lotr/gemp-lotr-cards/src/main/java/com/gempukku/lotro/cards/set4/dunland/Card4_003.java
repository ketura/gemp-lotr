package com.gempukku.lotro.cards.set4.dunland;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Dunland
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Skirmish: Spot 3 [DUNLAND] minions or a site you control to make a [DUNLAND] Man fierce until the regroup phase.
 */
public class Card4_003 extends AbstractOldEvent {
    public Card4_003() {
        super(Side.SHADOW, Culture.DUNLAND, "Anger", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && (
                Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.DUNLAND), Filters.type(CardType.MINION)) >= 3
                        || Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.siteControlled(playerId)));
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose DUNLAND Man", Filters.culture(Culture.DUNLAND), Filters.race(Race.MAN)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new AddUntilStartOfPhaseModifierEffect(
                                        new KeywordModifier(self, Filters.sameCard(card), Keyword.FIERCE), Phase.REGROUP));
                    }
                });
        return action;
    }
}
