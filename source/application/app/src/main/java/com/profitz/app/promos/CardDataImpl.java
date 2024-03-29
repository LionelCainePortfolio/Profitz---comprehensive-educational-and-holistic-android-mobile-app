package com.profitz.app.promos;

import com.profitz.app.R;import com.profitz.app.promos.adapters.ECCardData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CardDataImpl implements ECCardData<String> {

    private final String cardTitle;
    private final Integer mainBackgroundResource;
    private final Integer headBackgroundResource;
    private final List<String> listItems;

    public CardDataImpl(String cardTitle, Integer mainBackgroundResource, Integer headBackgroundResource, List<String> listItems) {
        this.mainBackgroundResource = mainBackgroundResource;
        this.headBackgroundResource = headBackgroundResource;
        this.listItems = listItems;
        this.cardTitle = cardTitle;
    }

    @Override
    public Integer getMainBackgroundResource() {
        return mainBackgroundResource;
    }

    @Override
    public Integer getHeadBackgroundResource() {
        return headBackgroundResource;
    }

    @Override
    public List<String> getListItems() {
        return listItems;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public static List<ECCardData> generateExampleData() {
        List<ECCardData> list = new ArrayList<>();
        list.add(new CardDataImpl("", R.drawable.level5_3, R.layout.level_zero, createItemsList("Card 1")));
        list.add(new CardDataImpl("Card 2", R.drawable.attractions, R.drawable.attractions_head, createItemsList("Card 2")));
        list.add(new CardDataImpl("Card 3", R.drawable.attractions, R.drawable.attractions_head, createItemsList("Card 3")));
        return list;
    }

    private static List<String> createItemsList(String cardName) {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(
                cardName + " - Item 1",
                cardName + " - Item 2",
                cardName + " - Item 3",
                cardName + " - Item 4",
                cardName + " - Item 5",
                cardName + " - Item 6",
                cardName + " - Item 7"
        ));
        return list;
    }

}