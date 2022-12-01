package com.wizardlybump17.wlib.command.args;

import com.wizardlybump17.wlib.command.args.reader.ArgsReader;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specified the {@link ArgsReader} of the parameter
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ArgsReaderType {

    /**
     * @return the class of the {@link ArgsReader} to use
     */
    Class<? extends ArgsReader<?>> value();
}
