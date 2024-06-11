package com.gildedrose;

class GildedRose {

    public static final String BACKSTAGEPASS_NAME = "Backstage passes to a TAFKAL80ETC concert";
    public static final String BRIE_NAME = "Aged Brie";
    public static final String SULFURAS_NAME = "Sulfuras, Hand of Ragnaros";
    Item[] items;

    public GildedRose(Item[] items) {
        this.items = items;
    }

    public void updateQuality() {

        for (int i = 0; i < items.length; i++) {
            if (!items[i].name.equals(BRIE_NAME)
                    && !items[i].name.equals(BACKSTAGEPASS_NAME)) {
                if (items[i].quality > 0) {
                    if (!items[i].name.equals(SULFURAS_NAME)) {
                        items[i].quality = items[i].quality - 1;
                    }
                }
            } else {
                if (items[i].quality < 50) {
                    items[i].quality = items[i].quality + 1;

                    if (items[i].name.equals(BACKSTAGEPASS_NAME)) {
                        if (items[i].sellIn < 11) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }

                        if (items[i].sellIn < 6) {
                            if (items[i].quality < 50) {
                                items[i].quality = items[i].quality + 1;
                            }
                        }
                    }
                }
            }

            if (!items[i].name.equals(SULFURAS_NAME)) {
                items[i].sellIn = items[i].sellIn - 1;
            }

            if (items[i].sellIn < 0) {
                if (!items[i].name.equals(BRIE_NAME)) {
                    if (!items[i].name.equals(BACKSTAGEPASS_NAME)) {
                        if (items[i].quality > 0) {
                            if (!items[i].name.equals(SULFURAS_NAME)) {
                                items[i].quality = items[i].quality - 1;
                            }
                        }
                    } else {
                        items[i].quality = items[i].quality - items[i].quality;
                    }
                } else {
                    if (items[i].quality < 50) {
                        items[i].quality = items[i].quality + 1;
                    }
                }
            }
        }
    }
}
