package com.gempukku.lotro.cards.set14.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Expanded Middle-earth
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 8
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. When you play this, choose one of the following keywords: battleground, mountain, plains,
 * or underground. The fellowship's current site gains that keyword until the end of the turn.
 */
public class Card14_015 extends AbstractMinion {
    public Card14_015() {
        super(3, 8, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk-hai Scout");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)) {
            final RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new PlayoutDecisionEffect(self.getOwner(),
                            new MultipleChoiceAwaitingDecision(1, "Choose type", new String[]{"battleground", "mountain", "plains", "underground"}) {
                                @Override
                                protected void validDecisionMade(int index, String result) {
                                    Keyword keyword;
                                    if (index == 0)
                                        keyword = Keyword.BATTLEGROUND;
                                    else if (index == 1)
                                        keyword = Keyword.MOUNTAIN;
                                    else if (index == 2)
                                        keyword = Keyword.PLAINS;
                                    else
                                        keyword = Keyword.UNDERGROUND;
                                    action.appendEffect(
                                            new AddUntilEndOfTurnModifierEffect(
                                                    new KeywordModifier(self, game.getGameState().getCurrentSite(), keyword)));
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
