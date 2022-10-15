package com.wizardlybump17.wlib.command.args.reader;

public class IntegerArrayReader extends ArgsReader<int[]> {

    @Override
    public Class<int[]> getType() {
        return int[].class;
    }

    @Override
    public int[] read(String string) throws ArgsReaderException {
        try {
            final String[] strings = string.split(" ");

            int[] result = new int[strings.length];
            for (int i = 0; i < result.length; i++)
                result[i] = Integer.parseInt(strings[i]);
            return result;
        } catch (NumberFormatException e) {
            throw new ArgsReaderException("expected a int array in string form but got " + string);
        }
    }
}
