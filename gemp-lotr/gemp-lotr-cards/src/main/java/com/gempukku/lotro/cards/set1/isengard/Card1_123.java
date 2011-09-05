package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Spell. Weather. Maneuver: Exert a [ISENGARD] minion and spot 5 companions to discard an exhausted
 * companion (except the Ring-bearer).
 */
public class Card1_123 extends AbstractEvent {
    public Card1_123() {
        super(Side.SHADOW, CardType.EVENT, Culture.ISENGARD, "Caradhras Has Not Forgiven Us", Phase.MANEUVER);
        addKeyword(Keyword.WEATHER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION), Filters.canExert())
                && Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION)) >= 5;
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseActiveCardEffect(playerId, "Choose an ISENGARD minion", Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION), Filters.canExert()) {
                    @Override
                    protected void cardSelected(PhysicalCard isengardMinion) {
                        action.addCost(new ExertCharacterEffect(isengardMinion));
                    }
                });
        action.addEffect(
                new ChooseActiveCardEffect(playerId, "Choose non Ring-bearer exhausted companion", Filters.type(CardType.COMPANION), Filters.not(Filters.keyword(Keyword.RING_BEARER)), Filters.not(Filters.canExert())) {
                    @Override
                    protected void cardSelected(PhysicalCard exhaustedCompanion) {
                        action.addEffect(new DiscardCardFromPlayEffect(exhaustedCompanion));
                    }
                }
        );
        return action;
    }
}
