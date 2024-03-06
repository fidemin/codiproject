package com.yunhongmin.codi.initialize;

import lombok.Getter;

import java.util.Arrays;

@Getter
public class CodiBrandData {
    private final String brandName;

    // total 8 elements required.
    // [TOP, OUTERWARE, PANTS, SNEAKERS, BAG, HAT, SOCKS, ACCESSORY]
    private final int[] prices;
    private final int totalPrice;

    public CodiBrandData(String brandName, int[] prices) {
        this.brandName = brandName;
        this.prices = prices;
        this.totalPrice = Arrays.stream(prices).sum();
    }
}
