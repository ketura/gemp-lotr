package com.gempukku.lotro.cards.set17.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.ReinforceTokenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Spot a Dwarf hunter and choose one: exert a minion for each hunter you can spot or reinforce
 * a [DWARVEN] token for each hunter you can spot.
 */
public class Card17_003 extends AbstractEvent {
    public Card17_003() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Dwarven Stratagem", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.DWARF, Keyword.HUNTER);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        final int count = Filters.countActive(game, Keyword.HUNTER);
        List<Effect> possibleEffects = new LinkedList<Effect>();
        possibleEffects.add(
                new UnrespondableEffect() {
                    @Override
                    public String getText(LotroGame game) {
                        return "Exert a minion for each hunter you can spot";
                    }

                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        for (int i = 0; i < count; i++)
                            action.appendEffect(
                                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, CardType.MINION));
                    }
                });
        possibleEffects.add(
                new UnrespondableEffect() {
                    @Override
                    public String getText(LotroGame game) {
                        return "Reinforce a DWARVEN token for each hunter you can spot";
                    }

                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        for (int i = 0; i < count; i++)
                            action.appendEffect(
                                    new ReinforceTokenEffect(self, playerId, Token.DWARVEN));
                    }
                });
        action.appendEffect(
                new ChoiceEffect(action, playerId, possibleEffects));
        return action;
    }
}
