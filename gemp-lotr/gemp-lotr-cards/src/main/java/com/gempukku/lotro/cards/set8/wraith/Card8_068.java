package com.gempukku.lotro.cards.set8.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseActionProxyEffect;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.PutCardsFromHandBeneathDrawDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.AbstractActionProxy;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.KillResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 3
 * Type: Event • Skirmish
 * Game Text: Exert a Nazgul to make him strength +2 and damage +1. If the character he is skirmishing is killed,
 * the Free Peoples player must place his or her hand beneath his or her draw deck.
 */
public class Card8_068 extends AbstractEvent {
    public Card8_068() {
        super(Side.SHADOW, 3, Culture.WRAITH, "Beyond All Darkness", Phase.SKIRMISH);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, Race.NAZGUL);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.NAZGUL) {
                    @Override
                    protected void forEachCardExertedCallback(final PhysicalCard character) {
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new StrengthModifier(self, character, 2), Phase.SKIRMISH));
                        action.appendEffect(
                                new AddUntilEndOfPhaseModifierEffect(
                                        new KeywordModifier(self, character, Keyword.DAMAGE, 1), Phase.SKIRMISH));

                        final Collection<PhysicalCard> againstNazgulCollection = Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Filters.inSkirmishAgainst(character));
                        if (againstNazgulCollection.size() > 0) {
                            final PhysicalCard againstNazgul = againstNazgulCollection.iterator().next();
                            action.appendEffect(
                                    new AddUntilEndOfPhaseActionProxyEffect(
                                            new AbstractActionProxy() {
                                                @Override
                                                public List<? extends Action> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult) {
                                                    if (effectResult.getType() == EffectResult.Type.KILL) {
                                                        KillResult killResult = (KillResult) effectResult;
                                                        if (killResult.getKilledCards().contains(againstNazgul)) {
                                                            RequiredTriggerAction action = new RequiredTriggerAction(self);
                                                            action.appendEffect(
                                                                    new PutCardsFromHandBeneathDrawDeckEffect(action, game.getGameState().getCurrentPlayerId()));
                                                            return Collections.singletonList(action);
                                                        }
                                                    }
                                                    return null;
                                                }
                                            }, Phase.SKIRMISH));
                        }
                    }
                });
        return action;
    }
}
