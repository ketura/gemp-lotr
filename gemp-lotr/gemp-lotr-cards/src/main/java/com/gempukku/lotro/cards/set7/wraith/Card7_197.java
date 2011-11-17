package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.FreePeoplePlayerMayNotAssignCharacterModifier;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 7
 * Type: Minion • Orc
 * Strength: 15
 * Vitality: 4
 * Site: 4
 * Game Text: For each Nazgul you can spot, the Free Peoples player must exert a companion to assign this minion
 * to a skirmish. Skirmish: Exert this minion to make a Nazgul or [WRAITH] Orc strength +1.
 */
public class Card7_197 extends AbstractMinion {
    public Card7_197() {
        super(7, 15, 4, 4, Race.ORC, Culture.WRAITH, "Morgul Regiment");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new FreePeoplePlayerMayNotAssignCharacterModifier(self,
                        new AndCondition(
                                new SpotCondition(Race.NAZGUL),
                                new Condition() {
                                    @Override
                                    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                                        return self.getData() == null;
                                    }
                                }), self));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.FREE_PEOPLE_PLAYER_STARTS_ASSIGNING
                && PlayConditions.canSpot(game, Race.NAZGUL)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            final int count = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Race.NAZGUL);
            action.appendCost(
                    new PlayoutDecisionEffect(game.getUserFeedback(), game.getGameState().getCurrentPlayerId(),
                            new MultipleChoiceAwaitingDecision(1, "Do you wish to exert " + count + " companions to be able to assign this minion?", new String[]{"Yes", "No"}) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    if (index == 0) {
                                        action.appendCost(
                                                new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), count, count, CardType.COMPANION));
                                        action.appendEffect(
                                                new UnrespondableEffect() {
                                                    @Override
                                                    protected void doPlayEffect(LotroGame game) {
                                                        self.storeData(new Object());
                                                    }
                                                });
                                    }
                                }
                            }));
            return Collections.singletonList(action);
        }
        if (TriggerConditions.endOfPhase(game, effectResult, Phase.ASSIGNMENT))
            self.removeData();
        return null;
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 1, Filters.or(Race.NAZGUL, Filters.and(Culture.WRAITH, Race.ORC))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
