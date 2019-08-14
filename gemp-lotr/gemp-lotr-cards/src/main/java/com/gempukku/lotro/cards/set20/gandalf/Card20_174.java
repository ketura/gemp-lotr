package com.gempukku.lotro.cards.set20.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.PlayUtils;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfTurnActionProxyEffect;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.KillEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * ❷ You Shall Not Pass! [Gan]
 * Event • Skirmish
 * Spell.
 * If you have initiative, you may play this event from your discard pile. If you do, place Gandalf in the dead pile at the end of this skirmish phase.
 * Discard four cards from hand to discard all minions skirmishing Gandalf.
 * <p/>
 * http://lotrtcg.org/coreset/gandalf/youshallnotpass(r3).jpg
 */
public class Card20_174 extends AbstractEvent {
    public Card20_174() {
        super(Side.FREE_PEOPLE, 2, Culture.GANDALF, "You Shall Not Pass!", Phase.SKIRMISH);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canDiscardCardsFromHandToPlay(self, game, self.getOwner(), 4, Filters.any);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 4, Filters.any));
        action.appendEffect(
                new DiscardCardsFromPlayEffect(self.getOwner(), self, CardType.MINION, Filters.inSkirmishAgainst(Filters.gandalf)));
        return action;
    }

    @Override
    public List<? extends Action> getPhaseActionsFromDiscard(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.isPhase(game, Phase.SKIRMISH)
                && PlayConditions.hasInitiative(game, Side.FREE_PEOPLE)
                && PlayUtils.checkPlayRequirements(game, self, Filters.any, 0, 0, false, false)) {
            final PlayEventAction playCardAction = getPlayEventCardAction(playerId, game, self, 0);
            playCardAction.appendEffect(
                    new AddUntilEndOfTurnActionProxyEffect(
                            new AbstractActionProxy() {
                                @Override
                                public List<? extends RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                                    if (TriggerConditions.endOfPhase(game, effectResult, Phase.SKIRMISH)) {
                                        RequiredTriggerAction action = new RequiredTriggerAction(self);
                                        PhysicalCard gandalf = Filters.findFirstActive(game, Filters.gandalf);
                                        if (gandalf != null)
                                            action.appendEffect(
                                                    new KillEffect(gandalf, KillEffect.Cause.CARD_EFFECT));
                                        return Collections.singletonList(action);
                                    }
                                    return null;
                                }
                            }
                    ));
            return Collections.singletonList(playCardAction);
        }
        return null;
    }

}
