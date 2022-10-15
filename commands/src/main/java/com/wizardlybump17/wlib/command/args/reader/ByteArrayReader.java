package com.wizardlybump17.wlib.command.args.reader;

public class ByteArrayReader extends ArgsReader<byte[]> {

    @Override
    public Class<byte[]> getType() {
        return byte[].class;
    }

    @Override
    public byte[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = string.split(" ");

            byte[] result = new byte[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Byte.parseByte(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a byte array in string form but got " + string);
        }
    }
}
