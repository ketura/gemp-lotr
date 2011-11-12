package com.gempukku.lotro.cards.set8.gondor;

import com.gempukku.lotro.cards.AbstractResponseEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChoiceEffect;
import com.gempukku.lotro.cards.effects.SpotEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExhaustCharactersEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event • Response
 * Game Text: If a [GONDOR] Wraith is about to be killed, discard him and either exhaust another [GONDOR] Wraith or spot
 * another exhausted [GONDOR] Wraith to prevent that.
 */
public class Card8_033 extends AbstractResponseEvent {
    public Card8_033() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "Ellesar's Edict");
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (PlayConditions.isGettingKilled(effect, game, Culture.GONDOR, Race.WRAITH)) {
            KillEffect killEffect = (KillEffect) effect;
            Collection<PhysicalCard> killedWraiths = Filters.filter(killEffect.getCharactersToBeKilled(), game.getGameState(), game.getModifiersQuerying(), Culture.GONDOR, Race.WRAITH);
            List<PlayEventAction> actions = new LinkedList<PlayEventAction>();
            for (PhysicalCard killedWraith : killedWraiths) {
                if (PlayConditions.canExert(self, game, Filters.not(killedWraith), Culture.GONDOR, Race.WRAITH)
                        || PlayConditions.canSpot(game, Filters.not(killedWraith), Culture.GONDOR, Race.WRAITH, Filters.exhausted)) {
                    PlayEventAction action = new PlayEventAction(self);
                    action.setText("Discard " + killedWraith.getBlueprint().getName());
                    action.appendCost(
                            new DiscardCardsFromPlayEffect(self, self));
                    List<Effect> possibleCosts = new LinkedList<Effect>();
                    possibleCosts.add(
                            new ChooseAndExhaustCharactersEffect(action, playerId, 1, 1, Filters.not(killedWraith), Culture.GONDOR, Race.WRAITH) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Exhaust another GONDOR Wraith";
                                }
                            });
                    possibleCosts.add(
                            new SpotEffect(1, Filters.not(killedWraith), Culture.GONDOR, Race.WRAITH, Filters.exhausted) {
                                @Override
                                public String getText(LotroGame game) {
                                    return "Spot another exhausted GONDOR Wraith";
                                }
                            });
                    action.appendCost(
                            new ChoiceEffect(action, playerId, possibleCosts));
                    actions.add(action);
                }
            }
            return actions;
        }
        return null;
    }
}
