package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.CheckLimitEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndRemoveCultureTokensFromCardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: Engine. Shadow: Play a besieger to place a [SAURON] token here. Skirmish: Remove a [SAURON] token from
 * a condition to make a [SAURON] Orc strength +1 (limit +2).
 */
public class Card7_281 extends AbstractPermanent {
    public Card7_281() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "Great Siege-towers");
        addKeyword(Keyword.ENGINE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canPlayFromHand(playerId, game, Keyword.BESIEGER)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game.getGameState().getHand(playerId), Keyword.BESIEGER));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.SAURON));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canRemoveTokens(game, Token.SAURON, 1, CardType.CONDITION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndRemoveCultureTokensFromCardEffect(self, playerId, Token.SAURON, 1, CardType.CONDITION));
            action.appendEffect(
                    new CheckLimitEffect(action, self, 2, Phase.SKIRMISH,
                            new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, 1, Culture.SAURON, Race.ORC)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
