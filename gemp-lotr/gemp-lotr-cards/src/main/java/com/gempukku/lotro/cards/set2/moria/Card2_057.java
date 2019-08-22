package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractResponseEvent;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.timing.results.SkirmishAboutToEndResult;

import java.util.Collections;
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
public class Card2_057 extends AbstractResponseEvent {
    public Card2_057() {
        super(Side.SHADOW, 1, Culture.MORIA, "Final Cry");
    }

    @Override
    public List<PlayEventAction> getOptionalInHandAfterActions(String playerId, final LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)
                && effectResult.getType() == EffectResult.Type.SKIRMISH_ABOUT_TO_END) {
            SkirmishAboutToEndResult checkSkirmish = (SkirmishAboutToEndResult) effectResult;
            for (PhysicalCard minion: checkSkirmish.getMinionsInvolved()) {
                if (minion.getBlueprint().getTitle().equals("The Balrog")
                        && Filters.filter(game.getGameState().getSkirmish().getShadowCharacters(), game,
                        Filters.hasAttached(Filters.name("Whip of Many Thongs"))).size() > 0) {
                    PlayEventAction action = new PlayEventAction(self);
                    action.appendEffect(
                            new WoundCharactersEffect(self, CardType.COMPANION, Filters.inSkirmish));
                    action.appendEffect(
                            new WoundCharactersEffect(self, CardType.COMPANION, Filters.inSkirmish));
                    return Collections.singletonList(action);
                }
            }
        }
        return null;
    }
    
    @Override
    public List<PlayEventAction> getOptionalInHandBeforeActions(String playerId, LotroGame game, Effect effect, final PhysicalCard self) {
        if (PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)
                && Filters.canSpot(game, Filters.name("The Balrog"), Filters.inSkirmish)
                && (TriggerConditions.isGettingKilled(effect, game, Filters.hasAttached(Filters.name("Whip of Many Thongs")))
                 || TriggerConditions.isGettingDiscarded(effect, game, Filters.hasAttached(Filters.name("Whip of Many Thongs"))))) {
            PlayEventAction action = new PlayEventAction(self);
                action.appendEffect(
                    new WoundCharactersEffect(self, CardType.COMPANION, Filters.inSkirmish));
                action.appendEffect(
                    new WoundCharactersEffect(self, CardType.COMPANION, Filters.inSkirmish));
            return Collections.singletonList(action);
        }
        return null;
    }        
}
