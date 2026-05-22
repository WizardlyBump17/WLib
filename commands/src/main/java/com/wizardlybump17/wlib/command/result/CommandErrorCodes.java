package com.wizardlybump17.wlib.command.result;

import org.jetbrains.annotations.NotNull;

public final class CommandErrorCodes {

    private CommandErrorCodes() {
    }

    public static final @NotNull String FORBIDDEN_NO_PERMISSION = "WLib:Forbidden/NoPermission";
    public static final @NotNull String FORBIDDEN_GENERIC = "WLib:Forbidden/Generic";

    public static final @NotNull String NOT_FOUND_NODE_NOT_FOUND = "WLib:NotFound/NodeNotFound";
    public static final @NotNull String NOT_FOUND_GENERIC = "WLib:NotFound/Generic";

    public static final @NotNull String NOT_IMPLEMENTED_NO_COMMAND_EXECUTOR = "WLib:NotImplemented/NoCommandExecutor";

    public static final @NotNull String BAD_REQUEST_GENERIC = "WLib:BadRequest/Generic";
    public static final @NotNull String BAD_REQUEST_PARSE_ERROR = "WLib:BadRequest/ParseError";
    public static final @NotNull String BAD_REQUEST_EMPTY_INPUT = "WLib:BadRequest/EmptyInput";

    public static final @NotNull String UNPROCESSABLE_CONTENT_INVALID_INPUT = "WLib:UnprocessableContent/InvalidInput";

    public static final @NotNull String CONFLICT_GENERIC = "WLib:Conflict/Generic";

    public static final @NotNull String GENERIC_ERROR_GENERIC = "WLib:GenericError/Generic";

    public static final @NotNull String INVALID_SENDER_GENERIC = "WLib:InvalidSender/Generic";

    public static final @NotNull String UNAUTHORIZED_GENERIC = "WLib:Unauthorized/Generic";
}
