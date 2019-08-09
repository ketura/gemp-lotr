package com.gempukku.lotro.cards.set30.shire;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.actions.PlayerReconcilesAction;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.logic.timing.AbstractSuccessfulEffect;
import com.gempukku.lotro.logic.timing.Effect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 8
 * Game Text: Burglar. Bilbo may not be overwhelmed by Gollum unless his strength
 * is tripled. Response: If Bilbo wins a skirmish, reconcile your hand (limit once per turn).
 */
 
public class Card30_046 extends AbstractCompanion {
    public Card30_046() {
        super(0, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Bilbo", "Reliable Companion", true);
			addKeyword(Keyword.BURGLAR);
			addKeyword(Keyword.CAN_START_WITH_RING);
    }
	
	@Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new OverwhelmedByMultiplierModifier(self, self,
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                return Filters.inSkirmishAgainst(Filters.gollum).accepts(game, self);
                            }
                        }, 3));
	}

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AbstractSuccessfulEffect() {
                        @Override
                        public String getText(LotroGame game) {
                            return "Reconcile hand";
                        }

                        @Override
                        public Effect.Type getType() {
                            return null;
                        }

                        @Override
                        public void playEffect(LotroGame game) {
                            game.getActionsEnvironment().addActionToStack(
                                    new PlayerReconcilesAction(game, playerId));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}