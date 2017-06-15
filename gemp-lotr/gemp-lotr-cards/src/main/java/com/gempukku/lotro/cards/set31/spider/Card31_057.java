package com.gempukku.lotro.cards.set31.spider;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.DiscardCardsFromPlayResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Spider
 * Twilight Cost: 3
 * Type: Minion • Spider
 * Strength: 9
 * Vitality: 3
 * Site: 5
 * Game Text: Fierce. Each time an Orc is killed or discarded from play (except during the regroup phase),
 * You may play a Spider. It is twilight cost -2 and strength +2 until the regroup phase.
 */
public class Card31_057 extends AbstractMinion {
    public Card31_057() {
        super(3, 9, 3, 5, Race.SPIDER, Culture.SMAUG, "Crazy Cob", null, true);
        addKeyword(Keyword.FIERCE);
    }

	@Override
  public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
	  if (TriggerConditions.forEachDiscardedFromPlay(game, effectResult, CardType.MINION, Culture.MEN)
				&& !PlayConditions.isPhase(game, Phase.REGROUP)
				&& PlayConditions.canPlayFromHand(playerId, game, -2, Race.SPIDER)) {
			final OptionalTriggerAction action = new OptionalTriggerAction(self);
      action.setTriggerIdentifier(self.getCardId()+"-"+((DiscardCardsFromPlayResult) effectResult).getDiscardedCard().getCardId());
      action.appendEffect(
              new ChooseAndPlayCardFromHandEffect(playerId, game, -2, Race.SPIDER) {
          @Override
          protected void afterCardPlayed(PhysicalCard cardPlayed) {
              action.appendEffect(
                      new AddUntilStartOfPhaseModifierEffect(
                              new StrengthModifier(self, cardPlayed, 2), Phase.REGROUP));
          }
      });
      return Collections.singletonList(action);
    }
    return null;
  }	
}
