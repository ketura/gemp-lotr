package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.modifiers.FreePeoplePlayerMayNotAssignCharacterModifier;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: While you can spot a Nazgul, the Free Peoples player must exert a companion to assign this minion
 * to a skirmish.
 */
public class Card7_200 extends AbstractMinion {
    public Card7_200() {
        super(4, 9, 2, 4, Race.ORC, Culture.WRAITH, "Morgul Spawn");
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
            action.appendCost(
                    new PlayoutDecisionEffect(game.getUserFeedback(), game.getGameState().getCurrentPlayerId(),
                            new MultipleChoiceAwaitingDecision(1, "Do you wish to exert a companion to be able to assign this minion?", new String[]{"Yes", "No"}) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    if (index == 0) {
                                        action.appendCost(
                                                new ChooseAndExertCharactersEffect(action, game.getGameState().getCurrentPlayerId(), 1, 1, CardType.COMPANION));
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
}
