package com.gempukku.lotro.game.actions.lotronly;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.cards.PhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.game.effects.ResolveSkirmishEffect;
import com.gempukku.lotro.game.effects.TriggeringResultEffect;
import com.gempukku.lotro.game.modifiers.ModifiersLogic;
import com.gempukku.lotro.game.effects.UnrespondableEffect;
import com.gempukku.lotro.game.timing.results.EndOfPhaseResult;
import com.gempukku.lotro.game.timing.results.SkirmishAboutToEndResult;
import com.gempukku.lotro.game.timing.results.StartOfPhaseResult;

import java.util.Set;

public class SkirmishPhaseAction extends SystemQueueAction {
    public SkirmishPhaseAction(final PhysicalCard fellowshipCharacter, final Set<PhysicalCard> shadowCharacters) {
        appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        game.getGameState().startSkirmish(fellowshipCharacter, shadowCharacters);
                        game.getGameState().setCurrentPhase(Phase.SKIRMISH);
                    }
                });
        appendEffect(
                new TriggeringResultEffect(null, new StartOfPhaseResult(Phase.SKIRMISH), "Start of skirmish phase"));
        appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        ((ModifiersLogic) game.getModifiersEnvironment()).signalStartOfPhase(Phase.SKIRMISH);
                        ((DefaultActionsEnvironment) game.getActionsEnvironment()).signalStartOfPhase(Phase.SKIRMISH);
                    }
                });
        appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        game.getActionsEnvironment().addActionToStack(new SkirmishActionProcedureAction());
                    }
                });
        appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        if (!game.getGameState().getSkirmish().isCancelled()) {
                            insertEffect(
                                    new ResolveSkirmishEffect());
                        }
                    }
                });
        appendEffect(
                new TriggeringResultEffect(null, new SkirmishAboutToEndResult(shadowCharacters), "Skirmish about to end"));
        appendEffect(
                new TriggeringResultEffect(null, new EndOfPhaseResult(Phase.SKIRMISH), "End of skirmish phase"));
        appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        game.getGameState().finishSkirmish();
                    }
                });

        appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        ((ModifiersLogic) game.getModifiersEnvironment()).signalEndOfPhase(Phase.SKIRMISH);
                        ((DefaultActionsEnvironment) game.getActionsEnvironment()).signalEndOfPhase(Phase.SKIRMISH);
                    }
                });
    }
}
