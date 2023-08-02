package com.gempukku.lotro.processes.turn.tribbles;

import com.gempukku.lotro.cards.CardDeck;
import com.gempukku.lotro.cards.CardNotFoundException;
import com.gempukku.lotro.cards.LotroCardBlueprint;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.PlayerOrder;
import com.gempukku.lotro.game.PlayerOrderFeedback;
import com.gempukku.lotro.processes.GameProcess;
import com.gempukku.lotro.cards.CardBlueprintLibrary;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class TribblesPlayerOrderProcess implements GameProcess {
    private final Map<String, Integer> _startingTribbles = new LinkedHashMap<>();
    private final PlayerOrderFeedback _playerOrderFeedback;
    private final LinkedList<String> _players = new LinkedList<>();
    private final Map<String, CardDeck> _decks;
    private final CardBlueprintLibrary _library;
    private String _firstPlayer;

    public TribblesPlayerOrderProcess(Map<String, CardDeck> decks, CardBlueprintLibrary library,
                                      PlayerOrderFeedback playerOrderFeedback) {
        _players.addAll(decks.keySet());
        Collections.shuffle(_players, ThreadLocalRandom.current());
        _decks = decks;
        _playerOrderFeedback = playerOrderFeedback;
        _library = library;
    }

    @Override
    public void process(DefaultGame game) {
        LinkedList<String> playersSelecting = new LinkedList<>(_players);

        while (playersSelecting.size() > 1) {
            _startingTribbles.clear();

            for (String player : playersSelecting) {
                List<String> playerDeckCards = _decks.get(player).getDrawDeckCards();
                String randomCard = playerDeckCards.get(new Random().nextInt(playerDeckCards.size()));
                try {
                    LotroCardBlueprint randomBlueprint = _library.getLotroCardBlueprint(randomCard);
                    game.getGameState().sendMessage(player + " drew " + randomBlueprint.getTitle());
                    int randomTribbleCount = randomBlueprint.getTribbleValue();
                    _startingTribbles.put(player, randomTribbleCount);
                } catch (CardNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            int highestTribble = Collections.max(_startingTribbles.values());

            playersSelecting.removeIf(player -> _startingTribbles.get(player) < highestTribble);
        }

        _firstPlayer = playersSelecting.get(0);

        _playerOrderFeedback.setPlayerOrder(new PlayerOrder(_players), _firstPlayer);
    }

    @Override
    public GameProcess getNextProcess() {
        return new TribblesStartOfRoundGameProcess(_firstPlayer);
    }
}
