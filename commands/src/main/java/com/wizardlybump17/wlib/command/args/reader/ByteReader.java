package com.wizardlybump17.wlib.command.args.reader;

public class ByteReader extends ArgsReader<Byte> {

    @Override
    public Class<Byte> getType() {
        return byte.class;
    }

    @Override
    public Byte read(String string) throws ArgsReaderException {
        try {
            return Byte.parseByte(string);
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a byte but got " + string);
        }
    }
}
