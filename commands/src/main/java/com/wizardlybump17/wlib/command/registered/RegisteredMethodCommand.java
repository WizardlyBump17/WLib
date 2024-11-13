package com.wizardlybump17.wlib.command.registered;

import com.wizardlybump17.wlib.command.Argument;
import com.wizardlybump17.wlib.command.Description;
import com.wizardlybump17.wlib.command.annotation.Command;
import com.wizardlybump17.wlib.command.args.ArgsNode;
import com.wizardlybump17.wlib.command.args.ArgsReaderRegistry;
import com.wizardlybump17.wlib.command.args.ArgsReaderType;
import com.wizardlybump17.wlib.command.args.reader.ArgsReader;
import com.wizardlybump17.wlib.command.data.AnnotationCommandData;
import com.wizardlybump17.wlib.command.exception.CommandException;
import com.wizardlybump17.wlib.command.executor.CommandExecutor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Getter
public class RegisteredMethodCommand extends RegisteredCommand {

    private final @NotNull Command annotation;
    private final @NotNull Object object;
    private final @NotNull Method method;
    private final @NotNull MethodHandle methodHandle;

    public RegisteredMethodCommand(@NotNull Command annotation, @NotNull Object object, @NotNull Method method) throws NoSuchMethodException, IllegalAccessException {
        super(
                new AnnotationCommandData(
                        annotation,
                        object
                ),
                new ArrayList<>()
        );
        this.annotation = annotation;
        this.object = object;
        this.method = method;
        methodHandle = MethodHandles.lookup().findVirtual(object.getClass(), method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()));
        prepareNodes();
    }

    @Override
    protected CommandExecutor createExecutor() {
        return (sender, args) -> {
            List<Object> objects = new ArrayList<>(args.values());
            objects.add(0, sender);
            try {
                methodHandle.invokeWithArguments(objects);
            } catch (Throwable e) {
                throw new CommandException("Error while executing the command " + getCommand().getExecution() + " with the arguments " + args, e);
            }
        };
    }

    protected void prepareNodes() {
        String[] commandArgs = getCommand().getExecution().split(" ");

        Class<?>[] types = method.getParameterTypes();
        Parameter[] parameters = method.getParameters();
        int index = 1; //skipping the first type because of the CommandSender
        for (String commandArg : commandArgs) {
            if (!isRequiredArgs(commandArg)) {
                getNodes().add(new ArgsNode(
                        commandArg,
                        false,
                        null,
                        null,
                        false
                ));
                continue;
            }

            Description description = parameters[index].getAnnotation(Description.class);

            ArgsReaderType argsReaderType = parameters[index].getAnnotation(ArgsReaderType.class);
            if (argsReaderType == null && Argument.class.isAssignableFrom(types[index]))
                throw new IllegalArgumentException("the \"" + commandArg + "\" argument requires the " + ArgsReaderType.class.getName() + " annotation");

            ArgsReader<?> reader;
            if (argsReaderType == null)
                reader = ArgsReaderRegistry.INSTANCE.getReader(types[index]);
            else
                reader = ArgsReaderRegistry.INSTANCE.get(argsReaderType.value());
            if (reader == null)
                throw new IllegalArgumentException("no reader found for " + types[index].getName());

            getNodes().add(new ArgsNode(
                    trim(commandArg),
                    true,
                    reader,
                    description == null ? null : description.value(),
                    Argument.class.isAssignableFrom(types[index])
            ));

            index++;
        }
    }

    @Override
    protected boolean isValidArgs(@NotNull LinkedHashMap<String, Object> args) {
        return args.size() + 1 == method.getParameterCount();
    }

    @Override
    public boolean isOwnedBy(@NotNull Object object) {
        return this.object == object;
    }

    private static String trim(String string) {
        return string.substring(1, string.length() - 1);
    }

    private static boolean isRequiredArgs(String string) {
        return string.startsWith("<") && string.endsWith(">");
    }
}