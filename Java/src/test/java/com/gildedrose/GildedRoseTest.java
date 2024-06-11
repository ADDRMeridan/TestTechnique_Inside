package com.gildedrose;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class GildedRoseTest {

    private static final int QUALITY_UPPER_LIMIT = 50;

    private static final Item SULFURAS = new Item("Sulfuras, Hand of Ragnaros", -1, QUALITY_UPPER_LIMIT + 30);

    private static final Item CONJURED = new Item("Conjured Mana Cake", 3, 6);
    private static final Item EXPIRED_CONJURED = new Item("Conjured Mana Cake", -2, 6);

    private static final Item NORMAL = new Item("Normal Item", 3, 5);
    private static final Item EXPIRED_NORMAL = new Item("Normal Item", -1, 3);

    private static final Item BRIE = new Item("Aged Brie", 1, QUALITY_UPPER_LIMIT - 2);

    private static final Item BACKSTAGEPASS_EARLY = new Item("Backstage passes to a TAFKAL80ETC concert", 11, 10);
    private static final Item BACKSTAGEPASS_LATE = new Item("Backstage passes to a TAFKAL80ETC concert", 5, QUALITY_UPPER_LIMIT - 4);
    private static final Item BACKSTAGEPASS_EXPIRED = new Item("Backstage passes to a TAFKAL80ETC concert", 0, 15);

    @Test
    void qualityIsNeverNegative() {

        Item[] items = new Item[] {
            new Item("foo", 0, 0),
            new Item("toto", -5, -10)
        };
        GildedRose app = new GildedRose(items);
        app.updateQuality();

        Arrays.stream(app.items)
            .filter(item -> item.quality < 0)
            .findAny()
            .ifPresent(item -> fail("L'objet " + item + " a une qualitée négative"));
    }

    @Test
    void sulfurasQualityDoesNotDegrade() {

        Item[] items = new Item[] {
            SULFURAS
        };
        int expected = SULFURAS.quality;

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        Arrays.stream(app.items)
            .filter(item -> item.quality != expected)
            .findAny()
            .ifPresent( item -> fail("Sulfuras a eu ca qualitée changée"));
    }

    @Test
    void normalItemDegradation() {

        Item[] items = new Item[] {
            NORMAL,
            EXPIRED_NORMAL
        };
        int expectedNormalQuality = NORMAL.quality - 1;
        int expectedNormalSellIn = NORMAL.sellIn - 1;
        int expectedExpiredNormalQuality = EXPIRED_NORMAL.quality - 2;
        int expectedExpiredNormalSellIn = EXPIRED_NORMAL.sellIn - 1;

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        Item normal = app.items[0];
        assertEquals(expectedNormalQuality, normal.quality);
        assertEquals(expectedNormalSellIn, normal.sellIn);
        Item expiredNormal = app.items[1];
        assertEquals(expectedExpiredNormalQuality, expiredNormal.quality);
        assertEquals(expectedExpiredNormalSellIn, expiredNormal.sellIn);
    }

    @Test
    void brieDegradation() {

        Item[] items = new Item[] {
            BRIE
        };

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        Item actual = app.items[0];
        assertEquals(QUALITY_UPPER_LIMIT - 1, actual.quality);
        assertEquals(0, actual.sellIn);

        app.updateQuality();
        assertEquals(QUALITY_UPPER_LIMIT, actual.quality);
        assertEquals(-1, actual.sellIn);

        app.updateQuality();
        assertEquals(QUALITY_UPPER_LIMIT, actual.quality);
        assertEquals(-2, actual.sellIn);
    }

    @Test
    void conjuredItemDegradation() {

        Item[] items = new Item[] {
            CONJURED,
            EXPIRED_CONJURED
        };
        int expectedConjuredQuality = CONJURED.quality - 2;
        int expectedConjuredSellIn = CONJURED.sellIn - 1;
        int expectedExpiredConjuredQuality = EXPIRED_CONJURED.quality - 4;
        int expectedExpiredConjuredSellIn = EXPIRED_CONJURED.sellIn - 1;

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        Item conjured = app.items[0];
        assertEquals(expectedConjuredQuality, conjured.quality);
        assertEquals(expectedConjuredSellIn, conjured.sellIn);
        Item expiredConjured = app.items[1];
        assertEquals(expectedExpiredConjuredQuality, expiredConjured.quality);
        assertEquals(expectedExpiredConjuredSellIn, expiredConjured.sellIn);
    }

    @Test
    void backstagePassDegradation() {

        Item[] items = new Item[] {
            BACKSTAGEPASS_EARLY,
            BACKSTAGEPASS_LATE,
            BACKSTAGEPASS_EXPIRED
        };
        int expectedEarlyQuality = BACKSTAGEPASS_EARLY.quality + 1;
        int expectedEarlySellIn = BACKSTAGEPASS_EARLY.sellIn - 1;
        int expectedLateQuality = BACKSTAGEPASS_LATE.quality + 3;
        int expectedLateSellIn = BACKSTAGEPASS_LATE.sellIn - 1;
        int expectedExpiredSellIn = BACKSTAGEPASS_EXPIRED.sellIn - 1;

        GildedRose app = new GildedRose(items);
        app.updateQuality();

        Item early = app.items[0];
        assertEquals(expectedEarlyQuality, early.quality);
        assertEquals(expectedEarlySellIn, early.sellIn);
        Item late = app.items[1];
        assertEquals(expectedLateQuality, late.quality);
        assertEquals(expectedLateSellIn, late.sellIn);
        Item expired = app.items[2];
        assertEquals(0, expired.quality);
        assertEquals(expectedExpiredSellIn, expired.sellIn);

        app.updateQuality();
        //Early rentre dans la periode <10j donc doit faire +2 en qualité
        assertEquals(expectedEarlyQuality + 2, early.quality);
        assertEquals(expectedEarlySellIn - 1, early.sellIn);
        //Late doit se retrouver a la limite max de qualité et pas la dépasser
        assertEquals(QUALITY_UPPER_LIMIT, late.quality);
        assertEquals(expectedLateSellIn - 1, late.sellIn);
        assertEquals(0, expired.quality);
        assertEquals(expectedExpiredSellIn - 1, expired.sellIn);
    }
}
