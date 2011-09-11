package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.CorruptRingBearerEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: To play, exert a [SAURON] Orc. Plays to your support area. If you can spot 5 burdens and the Free Peoples
 * player has no cards in his or her draw deck, the Ring-bearer is corrupted.
 */
public class Card1_252 extends AbstractPermanent {
    public Card1_252() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.SAURON, Zone.SHADOW_SUPPORT, "The Irresistible Shadow");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.race(Race.ORC), Filters.canExert());
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayPermanentAction action = super.getPlayCardAction(playerId, game, self, twilightModifier);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose a SAURON Orc", true, Filters.culture(Culture.SAURON), Filters.race(Race.ORC), Filters.canExert()));
        return action;
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (game.getGameState().getBurdens() >= 5
                && game.getGameState().getDeck(game.getGameState().getCurrentPlayerId()).size() == 0) {
            RequiredTriggerAction action = new RequiredTriggerAction(self, null, "Corrupt Ring-Bearer");
            action.addEffect(
                    new CorruptRingBearerEffect());
            return Collections.singletonList(action);
        }
        return null;
    }
}
