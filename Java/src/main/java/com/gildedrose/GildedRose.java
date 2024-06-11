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

        for (Item item : items) {
            //Normal & Sulfuras
            if (!item.name.equals(BRIE_NAME)
                    && !item.name.equals(BACKSTAGEPASS_NAME)) {
                if (item.quality > 0) {
                    if (!item.name.equals(SULFURAS_NAME)) {
                        item.quality = item.quality - 1;
                    }
                }
            } else {
                if (item.quality < 50) {
                    //Brie + backstagepass refining
                    item.quality = item.quality + 1;

                    if (item.name.equals(BACKSTAGEPASS_NAME)) {
                        //Backstage only
                        if (item.sellIn < 11) {
                            if (item.quality < 50) {
                                item.quality = item.quality + 1;
                            }
                        }

                        if (item.sellIn < 6) {
                            if (item.quality < 50) {
                                item.quality = item.quality + 1;
                            }
                        }
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
                        if (item.quality > 0) {
                            if (!item.name.equals(SULFURAS_NAME)) {
                                item.quality = item.quality - 1;
                            }
                        }
                    } else {
                        //Expired Backstagepass
                        item.quality = 0;
                    }
                } else {
                    //Brie refining
                    if (item.quality < 50) {
                        item.quality = item.quality + 1;
                    }
                }
            }
        }
    }
}
