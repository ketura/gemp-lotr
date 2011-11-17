package com.gempukku.lotro.cards.set10.shire;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;
import com.gempukku.lotro.logic.timing.results.PlayCardResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot 2 [SHIRE] companions. Each time a minion is played, you may exert a Hobbit companion
 * to exert that minion (limit once per turn).
 */
public class Card10_114 extends AbstractPermanent {
    public Card10_114() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.SHIRE, Zone.SUPPORT, "Shadowplay");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, 2, Culture.SHIRE, CardType.COMPANION);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, CardType.MINION)
                && self.getData() == null
                && PlayConditions.canExert(self, game, Race.HOBBIT, CardType.COMPANION)) {
            PlayCardResult playResult = (PlayCardResult) effectResult;
            PhysicalCard playedCard = playResult.getPlayedCard();

            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Race.HOBBIT, CardType.COMPANION));
            action.appendEffect(
                    new UnrespondableEffect() {
                        @Override
                        protected void doPlayEffect(LotroGame game) {
                            self.storeData(new Object());
                        }
                    });
            action.appendEffect(
                    new ExertCharactersEffect(self, playedCard));
            return Collections.singletonList(action);
        }
        return null;
    }
}
