package com.gempukku.lotro.effects;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.processes.lotronly.skirmish.Skirmish;
import com.gempukku.lotro.actions.lotronly.SkirmishPhaseAction;

import java.util.Set;

public class AdditionalSkirmishPhaseEffect extends UnrespondableEffect {
    private final LotroPhysicalCard _fpCard;
    private final Set<LotroPhysicalCard> _shadowCards;

    public AdditionalSkirmishPhaseEffect(LotroPhysicalCard fpCard, Set<LotroPhysicalCard> shadowCards) {
        _fpCard = fpCard;
        _shadowCards = shadowCards;
    }

    @Override
    protected void doPlayEffect(DefaultGame game) {
        final Phase currentPhase = game.getGameState().getCurrentPhase();
        final Skirmish skirmish = game.getGameState().getSkirmish();
        final boolean fierce = game.getGameState().isFierceSkirmishes();
        final boolean extra = game.getGameState().isExtraSkirmishes();

        if (skirmish != null)
            game.getGameState().finishSkirmish();

        final SkirmishPhaseAction skirmishPhaseAction = new SkirmishPhaseAction(_fpCard, _shadowCards);
        // We have to reset the phase, to whatever it was before
        skirmishPhaseAction.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        // We might have to restart an existing skirmish
                        if (skirmish != null)
                            game.getGameState().restartSkirmish(skirmish);
                        game.getGameState().setCurrentPhase(currentPhase);
                        game.getGameState().setFierceSkirmishes(fierce);
                        game.getGameState().setExtraSkirmishes(extra);
                    }
                });

        game.getGameState().setFierceSkirmishes(false);
        game.getGameState().setExtraSkirmishes(true);
        game.getActionsEnvironment().addActionToStack(
                skirmishPhaseAction);
    }
}
