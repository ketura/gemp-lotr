package com.gempukku.lotro.decisions;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public abstract class ArbitraryCardsSelectionDecision extends AbstractAwaitingDecision {
    private final Collection<? extends LotroPhysicalCard> _physicalCards;
    private final Collection<? extends LotroPhysicalCard> _selectable;
    private final int _minimum;
    private final int _maximum;

    public ArbitraryCardsSelectionDecision(int id, String text, Collection<? extends LotroPhysicalCard> physicalCard) {
        this(id, text, physicalCard, 0, physicalCard.size());
    }

    public ArbitraryCardsSelectionDecision(int id, String text, Collection<? extends LotroPhysicalCard> physicalCards, int minimum, int maximum) {
        this(id, text, physicalCards, physicalCards, minimum, maximum);
    }

    public ArbitraryCardsSelectionDecision(int id, String text, Collection<? extends LotroPhysicalCard> physicalCards, Collection<? extends LotroPhysicalCard> selectable, int minimum, int maximum) {
        super(id, text, AwaitingDecisionType.ARBITRARY_CARDS);
        _physicalCards = physicalCards;
        _selectable = selectable;
        _minimum = minimum;
        _maximum = maximum;
        setParam("min", String.valueOf(minimum));
        setParam("max", String.valueOf(maximum));
        setParam("cardId", getCardIds(physicalCards));
        setParam("blueprintId", getBlueprintIds(physicalCards));
        setParam("selectable", getSelectable(physicalCards, selectable));
    }

    private String[] getSelectable(Collection<? extends LotroPhysicalCard> physicalCards, Collection<? extends LotroPhysicalCard> selectable) {
        String[] result = new String[physicalCards.size()];
        int index = 0;
        for (LotroPhysicalCard physicalCard : physicalCards) {
            result[index] = String.valueOf(selectable.contains(physicalCard));
            index++;
        }
        return result;
    }

    private String[] getCardIds(Collection<? extends LotroPhysicalCard> physicalCards) {
        String[] result = new String[physicalCards.size()];
        for (int i = 0; i < physicalCards.size(); i++)
            result[i] = "temp" + i;
        return result;
    }

    private String[] getBlueprintIds(Collection<? extends LotroPhysicalCard> physicalCards) {
        String[] result = new String[physicalCards.size()];
        int index = 0;
        for (LotroPhysicalCard physicalCard : physicalCards) {
            result[index] = physicalCard.getBlueprintId();
            index++;
        }
        return result;
    }

    protected LotroPhysicalCard getPhysicalCardByIndex(int index) {
        int i = 0;
        for (LotroPhysicalCard physicalCard : _physicalCards) {
            if (i == index)
                return physicalCard;
            i++;
        }
        return null;
    }

    protected List<LotroPhysicalCard> getSelectedCardsByResponse(String response) throws DecisionResultInvalidException {
        String[] cardIds;
        if (response.equals(""))
            cardIds = new String[0];
        else
            cardIds = response.split(",");

        if (cardIds.length < _minimum || cardIds.length > _maximum)
            throw new DecisionResultInvalidException();

        List<LotroPhysicalCard> result = new LinkedList<>();
        try {
            for (String cardId : cardIds) {
                LotroPhysicalCard card = getPhysicalCardByIndex(Integer.parseInt(cardId.substring(4)));
                if (result.contains(card) || !_selectable.contains(card))
                    throw new DecisionResultInvalidException();
                result.add(card);
            }
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            throw new DecisionResultInvalidException();
        }

        return result;
    }
}

