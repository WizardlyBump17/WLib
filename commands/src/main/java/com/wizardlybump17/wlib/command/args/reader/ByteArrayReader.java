package com.wizardlybump17.wlib.command.args.reader;

public class ByteArrayReader extends ArrayReader<Byte[]> {

    @Override
    public Class<Byte[]> getType() {
        return Byte[].class;
    }

    @Override
    public Byte[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = split(string);

            if (strings == null)
                throw new NumberFormatException();

            Byte[] result = new Byte[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Byte.parseByte(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a byte array in string form but got " + string);
        }
    }
}
