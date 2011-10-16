package com.gempukku.lotro.logic.timing.processes.turn.archery;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.processes.GameProcess;

public class ArcheryFireGameProcess implements GameProcess {
    private LotroGame _game;
    private GameProcess _followingGameProcess;

    private int _fellowshipArcheryTotal;
    private int _shadowArcheryTotal;

    public ArcheryFireGameProcess(LotroGame game, GameProcess followingGameProcess) {
        _game = game;
        _followingGameProcess = followingGameProcess;
    }

    @Override
    public void process() {
        GameState gameState = _game.getGameState();
        _fellowshipArcheryTotal = Filters.countActive(gameState, _game.getModifiersQuerying(),
                Filters.keyword(Keyword.ARCHER),
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return modifiersQuerying.addsToArcheryTotal(gameState, physicalCard);
                    }
                },
                Filters.or(
                        Filters.type(CardType.COMPANION),
                        Filters.and(
                                Filters.type(CardType.ALLY),
                                Filters.or(
                                        Filters.and(
                                                Filters.allyAtHome,
                                                new Filter() {
                                                    @Override
                                                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                                        return !modifiersQuerying.isAllyPreventedFromParticipatingInArcheryFire(gameState, physicalCard);
                                                    }
                                                }),
                                        new Filter() {
                                            @Override
                                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                                return modifiersQuerying.isAllyParticipateInArcheryFire(gameState, physicalCard);
                                            }
                                        })
                        )
                ));

        _fellowshipArcheryTotal = _game.getModifiersQuerying().getArcheryTotal(gameState, Side.FREE_PEOPLE, _fellowshipArcheryTotal);

        _shadowArcheryTotal = Filters.countActive(gameState, _game.getModifiersQuerying(),
                Filters.keyword(Keyword.ARCHER),
                new Filter() {
                    @Override
                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                        return modifiersQuerying.addsToArcheryTotal(gameState, physicalCard);
                    }
                },
                Filters.type(CardType.MINION));

        _shadowArcheryTotal = _game.getModifiersQuerying().getArcheryTotal(gameState, Side.SHADOW, _shadowArcheryTotal);
    }

    @Override
    public GameProcess getNextProcess() {
        return new FellowshipPlayerAssignsArcheryDamageGameProcess(_game, _shadowArcheryTotal,
                new FellowshipPlayerChoosesShadowPlayerToAssignDamageToGameProcess(_game, _fellowshipArcheryTotal, _followingGameProcess));
    }
}
