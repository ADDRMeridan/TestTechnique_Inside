package com.gildedrose;

import java.util.Set;
import java.util.function.Predicate;

class GildedRose {

    public static final int QUALITY_UPPER_LIMIT = 50;

    public static final String BACKSTAGEPASS_NAME = "Backstage passes to a TAFKAL80ETC concert";
    public static final String BRIE_NAME = "Aged Brie";
    public static final String CONJURED_NAME = "Conjured Mana Cake";
    public static final String SULFURAS_NAME = "Sulfuras, Hand of Ragnaros";

    public static final Set<String> SPECIFIC_ITEMS = Set.of(BACKSTAGEPASS_NAME, BRIE_NAME, SULFURAS_NAME);
    //Items that don't have a selling deadline and never see their quality change
    public static final Set<String> ETERNAL_ITEMS = Set.of(SULFURAS_NAME);

    Item[] items;
    private Predicate<Item> canBeRefined = item -> item.quality < QUALITY_UPPER_LIMIT;

    private Predicate<Item> isEternalItem = item -> ETERNAL_ITEMS.contains(item.name);
    private Predicate<Item> isSpecificItem = item -> SPECIFIC_ITEMS.contains(item.name);
    private Predicate<Item> isConjuredItem = item -> CONJURED_NAME.equals(item.name);

    public GildedRose(Item[] items) {

        this.items = items;
    }

    private void updateNormalOrConjuredQuality(Item item) {

    }

    public void updateQuality() {

        for (Item item : items) {
            if (isEternalItem.test(item)) {
                continue; //We can skip eternal items as they don't change
            }

            if (!isSpecificItem.test(item)) {
                updateQualityNormalOrConjuredItem(item);
            } else {
                //Brie + backstagepass refining
                item.quality = item.quality + 1;

                if (item.name.equals(BACKSTAGEPASS_NAME)) {
                    //Backstage only
                    if (item.sellIn < 11) {
                        item.quality = item.quality + 1;
                    }

                    if (item.sellIn < 6) {
                        item.quality = item.quality + 1;
                    }
                }
            }

            //All except Sulfuras
            if (!item.name.equals(SULFURAS_NAME)) {
                item.sellIn = item.sellIn - 1;
            }

            if (item.sellIn < 0) {
                if (!item.name.equals(BRIE_NAME)) {
                    if (!item.name.equals(BACKSTAGEPASS_NAME)) {
                        updateQualityNormalOrConjuredItem(item);
                    } else {
                        //Expired Backstagepass
                        item.quality = 0;
                    }
                } else {
                    //Brie refining
                    item.quality = item.quality + 1;
                }
            }

            //Manage Quality bounds
            if (!SULFURAS_NAME.equals(item.name)) {
                item.quality = Integer.min(QUALITY_UPPER_LIMIT, item.quality); //Max quality
                item.quality = Integer.max(0, item.quality); //Min quality
            }
        }
    }

    private void updateQualityNormalOrConjuredItem(Item item) {

        item.quality--;
        if (isConjuredItem.test(item)) {
            //Conjured Item degrades twice as fast
            item.quality--;
        }
    }
}
