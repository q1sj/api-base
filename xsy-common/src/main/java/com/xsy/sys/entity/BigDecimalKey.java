package com.xsy.sys.entity;

import java.math.BigDecimal;

/**
 * @author Q1sj
 * @date 2022.10.20 15:37
 */
public class BigDecimalKey extends BaseKey<BigDecimal> {

    public BigDecimalKey(String key) {
        super(key);
    }

    public BigDecimalKey(String key, BigDecimal defaultValue) {
        super(key, defaultValue);
    }

    @Override
    protected String serializationNotNull(BigDecimal val) {
        return val.toString();
    }

    @Override
    protected BigDecimal deserializationNotNull(String val) {
        return new BigDecimal(val);
    }
}
