package com.mfpe;

import java.math.BigDecimal;

public class PriceUpdate {

    private String symbol;
    private BigDecimal lastPrice;

    public PriceUpdate() {
    }

    public PriceUpdate(String symbol, BigDecimal lastPrice) {
        this.symbol = symbol;
        this.lastPrice = lastPrice;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(BigDecimal lastPrice) {
        this.lastPrice = lastPrice;
    }
}
