package com.gempukku.lotro.cards.set2.gandalf;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PreventableEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Arrays;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Spell. Maneuver: Spot Gandalf to make a companion defender +1 until the regroup phase. Any Shadow player
 * may remove (3) to prevent this.
 */
public class Card2_028 extends AbstractEvent {
    public Card2_028() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Wielder of the Flame", Phase.MANEUVER);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PreventableEffect(action,
                        new ChooseActiveCardEffect(self, playerId, "Choose companion", CardType.COMPANION) {
                            @Override
                            protected void cardSelected(LotroGame game, PhysicalCard card) {
                                action.appendEffect(
                                        new AddUntilStartOfPhaseModifierEffect(
                                                new KeywordModifier(self, Filters.sameCard(card), Keyword.DEFENDER), Phase.REGROUP));
                            }

                            @Override
                            public String getText(LotroGame game) {
                                return "Make a companion Defender +1";
                            }
                        },
                        Arrays.asList(GameUtils.getShadowPlayers(game)),
                        new PreventableEffect.PreventionCost() {
                            @Override
                            public Effect createPreventionCostForPlayer(CostToEffectAction subAction, String playerId) {
                                return new RemoveTwilightEffect(3);
                            }
                        }
                ));
        return action;
    }
}
