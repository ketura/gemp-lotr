package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.cards.AbstractResponseOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Response: If a skirmish that involved The Balrog bearing Whip of Many Thongs is about to end, wound
 * a companion in that skirmish twice.
 */
public class Card2_057 extends AbstractResponseOldEvent {
    public Card2_057() {
        super(Side.SHADOW, Culture.MORIA, "Final Cry");
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public List<PlayEventAction> getOptionalBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
//        if (PlayConditions.canPlayCardDuringPhase(game, (Phase) null, self)) {
//            if (effect.getType() == EffectResult.Type.END_OF_PHASE
//                    && game.getGameState().getCurrentPhase() == Phase.SKIRMISH
//                    && Filters.filter(game.getGameState().getSkirmish().getShadowCharacters(), game.getGameState(), game.getModifiersQuerying(), Filters.name("The Balrog"), Filters.hasAttached(Filters.name("Whip of Many Thongs"))).size() > 0) {
//                PlayEventAction action = new PlayEventAction(self);
//                action.appendEffect(
//                        new WoundCharactersEffect(self, Filters.and(Filters.type(CardType.COMPANION), Filters.inSkirmish)));
//                action.appendEffect(
//                        new WoundCharactersEffect(self, Filters.and(Filters.type(CardType.COMPANION), Filters.inSkirmish)));
//                return Collections.singletonList(action);
//            }
//        }
        return null;
    }
}
