package com.wizardlybump17.wlib.command.args.reader;

public class IntegerArrayReader extends ArrayReader<Integer[]> {

    @Override
    public Class<Integer[]> getType() {
        return Integer[].class;
    }

    @Override
    public Integer[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = split(string);
            if (strings == null)
                throw new NumberFormatException();

            Integer[] result = new Integer[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Integer.parseInt(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a int array in string form but got " + string);
        }
    }
}
