package com.wizardlybump17.wlib.command.result;

import org.jetbrains.annotations.NotNull;

public final class ErrorCodes {

    private ErrorCodes() {
    }

    public static final @NotNull String FORBIDDEN_NO_PERMISSION = "WLib:Forbidden/NoPermission";
    public static final @NotNull String NOT_FOUND_NODE_NOT_FOUND = "WLib:NotFound/NodeNotFound";
    public static final @NotNull String NOT_IMPLEMENTED_NO_COMMAND_EXECUTOR = "WLib:NotImplemented/NoCommandExecutor";
    public static final @NotNull String BAD_REQUEST_PARSE_ERROR = "WLib:BadRequest/ParseError";
    public static final @NotNull String BAD_REQUEST_EMPTY_INPUT = "WLib:BadRequest/EmptyInput";
    public static final @NotNull String UNPROCESSABLE_CONTENT_INVALID_INPUT = "WLib:UnprocessableContent/InvalidInput";
}
