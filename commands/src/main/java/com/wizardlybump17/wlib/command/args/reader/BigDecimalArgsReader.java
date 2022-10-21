package com.wizardlybump17.wlib.command.args.reader;

import java.math.BigDecimal;

public class BigDecimalArgsReader extends ArgsReader<BigDecimal> {

    @Override
    public Class<BigDecimal> getType() {
        return BigDecimal.class;
    }

    @Override
    public BigDecimal read(String string) {
        try {
            return new BigDecimal(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
