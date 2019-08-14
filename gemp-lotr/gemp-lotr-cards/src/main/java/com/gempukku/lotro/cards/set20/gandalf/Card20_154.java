package com.gempukku.lotro.cards.set20.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CardMatchesEvaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * 1
 * Bolstered Spirits
 * Event • Skirmish
 * Spot Gandalf to make a companion strength +2 (or strength +3 if that companion has 4 or more resistance).
 * http://www.lotrtcg.org/coreset/gandalf/bolsteredspirits(r2).jpg
 */
public class Card20_154 extends AbstractEvent {
    public Card20_154() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Bolstered Spirits", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new CardMatchesEvaluator(2, 3,
                                new Filter() {
                                    @Override
                                    public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                        return game.getModifiersQuerying().getResistance(game, physicalCard)>=4;
                                    }
                                }), CardType.COMPANION));
        return action;
    }
}
