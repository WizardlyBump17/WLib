package com.wizardlybump17.wlib.command.args.reader;

import java.math.BigInteger;

public class BigIntegerArgsReader extends ArgsReader<BigInteger> {

    @Override
    public Class<BigInteger> getType() {
        return BigInteger.class;
    }

    @Override
    public BigInteger read(String string) {
        try {
            return new BigInteger(string);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
