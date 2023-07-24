package com.gempukku.lotro.game.adventure;

import com.gempukku.lotro.cards.CardBlueprintLibrary;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.rules.GameUtils;
import com.gempukku.lotro.game.actions.lotronly.SystemQueueAction;
import com.gempukku.lotro.game.timing.PlayerOrderFeedback;
import com.gempukku.lotro.game.effects.UnrespondableEffect;
import com.gempukku.lotro.game.timing.processes.GameProcess;
import com.gempukku.lotro.game.timing.processes.pregame.lotronly.SetupSoloAdventureGameProcess;
import com.gempukku.lotro.game.timing.processes.lotronly.ShadowPhaseOfAIPlayerGameProcess;
import com.gempukku.lotro.game.timing.processes.lotronly.ai.AIPlayerAssignsArcheryTotalGameProcess;
import com.gempukku.lotro.game.timing.processes.lotronly.ai.AIPlayerAssignsMinionsGameProcess;
import com.gempukku.lotro.game.timing.processes.lotronly.regroup.DiscardAllMinionsGameProcess;
import com.gempukku.lotro.game.timing.processes.lotronly.regroup.ReturnFollowersToSupportGameProcess;
import com.gempukku.lotro.game.timing.results.PlayCardResult;

import java.util.Set;

public class DefaultSoloAdventure extends SoloAdventure {
    private final CardBlueprintLibrary _library;
    private final SiteSelection _siteSelection;
    private final ShadowAI _shadowAI;
    private final String _adventureCard;
    private final String _startingSite;

    public DefaultSoloAdventure(CardBlueprintLibrary library,
                                SiteSelection siteSelection, ShadowAI shadowAI,
                                String adventureCard, String startingSite) {
        _library = library;
        _siteSelection = siteSelection;
        _shadowAI = shadowAI;
        _adventureCard = adventureCard;
        _startingSite = startingSite;
    }

    @Override
    public void appendNextSiteAction(SystemQueueAction action) {
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(DefaultGame game) {
                        final GameState gameState = game.getGameState();
                        try {
                            LotroPhysicalCard newSite = gameState.createPhysicalCard("AI", _library, _siteSelection.getNextSite(game));
                            newSite.setSiteNumber(gameState.getCurrentSiteNumber() + 1);
                            gameState.addCardToZone(game, newSite, Zone.ADVENTURE_PATH);
                            gameState.sendMessage(newSite.getOwner() + " plays " + GameUtils.getCardLink(newSite));

                            game.getActionsEnvironment().emitEffectResult(new PlayCardResult(Zone.ADVENTURE_DECK, newSite, null, null));
                        } catch (CardNotFoundException exp) {
                            throw new RuntimeException("Unable to create a requested card", exp);
                        }
                    }
                });
    }

    @Override
    public GameProcess getStartingGameProcess(Set<String> players, PlayerOrderFeedback playerOrderFeedback) {
        final String player = players.iterator().next();
        return new SetupSoloAdventureGameProcess(_adventureCard, _startingSite, player, playerOrderFeedback);
    }

    @Override
    public GameProcess getAfterFellowshipPhaseGameProcess() {
        return new ShadowPhaseOfAIPlayerGameProcess(_shadowAI);
    }

    @Override
    public GameProcess getAfterFellowshipArcheryGameProcess(int fellowshipArcheryTotal, GameProcess followingProcess) {
        return new AIPlayerAssignsArcheryTotalGameProcess(fellowshipArcheryTotal, followingProcess);
    }

    @Override
    public GameProcess getAfterFellowshipAssignmentGameProcess(Set<LotroPhysicalCard> leftoverMinions, GameProcess followingProcess) {
        return new AIPlayerAssignsMinionsGameProcess(leftoverMinions, followingProcess);
    }

    @Override
    public GameProcess getBeforeFellowshipChooseToMoveGameProcess(GameProcess followingProcess) {
        return followingProcess;
    }

    @Override
    public GameProcess getPlayerStaysGameProcess(DefaultGame game, GameProcess followingProcess) {
        return new ReturnFollowersToSupportGameProcess(
                new DiscardAllMinionsGameProcess(followingProcess));
    }

    @Override
    public boolean isSolo() {
        return true;
    }
}
