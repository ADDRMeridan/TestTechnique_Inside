package com.gildedrose;

import java.util.Set;
import java.util.function.Predicate;

class GildedRose {

    public static final int QUALITY_UPPER_LIMIT = 50;

    public static final String BACKSTAGEPASS_NAME = "Backstage passes to a TAFKAL80ETC concert";
    public static final String BRIE_NAME = "Aged Brie";
    public static final String CONJURED_NAME = "Conjured Mana Cake";
    public static final String SULFURAS_NAME = "Sulfuras, Hand of Ragnaros";

    //Items that do not degrade in quality or with a specific update
    public static final Set<String> SPECIFIC_ITEMS = Set.of(BACKSTAGEPASS_NAME, BRIE_NAME, SULFURAS_NAME);
    //Items that don't have a selling deadline and never see their quality change
    public static final Set<String> ETERNAL_ITEMS = Set.of(SULFURAS_NAME);

    private final Predicate<Item> isBackstagePass = item -> BACKSTAGEPASS_NAME.equals(item.name);
    private final Predicate<Item> isEternalItem = item -> ETERNAL_ITEMS.contains(item.name);
    private final Predicate<Item> isSpecificItem = item -> SPECIFIC_ITEMS.contains(item.name);
    private final Predicate<Item> isConjuredItem = item -> CONJURED_NAME.equals(item.name);
    Item[] items;

    public GildedRose(Item[] items) {

        this.items = items;
    }

    public void updateQuality() {

        for (Item item : items) {
            //We can skip eternal items as they don't change
            if (isEternalItem.test(item)) {
                continue;
            }

            if (!isSpecificItem.test(item)) {
                updateQualityNormalOrConjuredItem(item);
            } else {
                //Brie + backstagepass refining
                item.quality++;

                if (isBackstagePass.test(item) && item.sellIn < 11) {
                    item.quality++;
                    if (item.sellIn < 6) {
                        item.quality++;
                    }
                }
            }

            //Selling deadline update
            item.sellIn = item.sellIn - 1;

            //Manage expired items
            if (item.sellIn < 0) {
                switch (item.name) {
                    case BRIE_NAME:
                        item.quality++;
                        break;
                    case BACKSTAGEPASS_NAME:
                        item.quality = 0;
                        break;
                    default:
                        updateQualityNormalOrConjuredItem(item);
                        break;
                }
            }

            //Manage Quality bounds
            item.quality = constrainIntToBounds(item.quality, 0, QUALITY_UPPER_LIMIT);
        }
    }

    /**
     * Constrains the given int within the given bounds
     *
     * @param toConstrain int that must be constrained
     * @param lowerBound  must be lower than upperBound
     * @param upperBound  must be higher than lowerBound
     * @return lowerbound < toConstrain < upperBound = toConstrain
     * <br/>toConstrain < lowerBound = lowerBound
     * <br/>upperBound < toConstrain = upperBound
     */
    private int constrainIntToBounds(int toConstrain, int lowerBound, int upperBound) {

        return Integer.max(lowerBound, Integer.min(upperBound, toConstrain));
    }

    private void updateQualityNormalOrConjuredItem(Item item) {

        item.quality--;
        if (isConjuredItem.test(item)) {
            //Conjured Item degrades twice as fast
            item.quality--;
        }
    }
}
