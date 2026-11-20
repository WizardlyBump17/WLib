# AGENTS.md — WLib

Guidance for AI agents working in this repository.

## 1. What this project is

**WLib** is a multi-module Java library + plugin for Minecraft servers. It ships two jars:

| Artifact | Platform | Built from |
|---|---|---|
| `WLib-<version>.jar` | Spigot/Paper Bukkit plugin | `core` (shades every other Bukkit-safe module into it) |
| `WLib-BungeeCord-<version>.jar` | BungeeCord plugin | `bungee` |

Maven coordinates: `com.wizardlybump17.wlib:<module>`, version `1.6.8-ncs-2-1.20.6+` (root `build.gradle:12-13`; CI appends `-SNAPSHOT.<n>` when `WLIB_SNAPSHOT=true`). Java **21** toolchain (`build.gradle:37-41`). Third-party plugins consume WLib as a `provided`/`compileOnly` dependency and declare `softdepend: [WLib]` in their `plugin.yml`.

Key facts agents must know up front:

- **Lombok** (`@Data`, `@UtilityClass`, `@Builder`, `@Accessors(chain=true)`, `@AllArgsConstructor`) is used pervasively, almost always as `compileOnly` + `annotationProcessor`. Generated getters/builders look "missing" in source — they are real at compile time.
- **`compileOnly` is the default scope** for almost everything (Paper API, BungeeCord API, Adventure, annotations). Runtime deps are supplied by the server (Bukkit `libraries:` in `plugin.yml`) or by the consuming plugin.
- Most registries/singletons are hand-rolled `getInstance()` singletons (`DatabaseRegister`, `ConfigHandlerRegistry`, `ConfigHolderFactoryRegistry`, `ControllerCache.INSTANCE`) and adapters are static service locators (`ItemAdapter.setInstance(...)` / `getInstance()`).

## 2. Repository map

```
core/          Bukkit plugin: main class, Bukkit glue for commands, inventory GUIs, ItemBuilder, listeners
commands/      Platform-agnostic Brigadier-like command framework (the biggest module, ~84 files)
database/      Database abstraction (MySQL/SQLite) + DAO/Controller player-caching pattern
config/        Annotation-driven config binding (@ConfigInfo/@Path → static fields), platform-neutral
objects/       Tiny data structures: Pair, Triple, Cache, Registry
utils/         Pure static helpers (strings, collections, reflection, dates, random...)
bukkit-utils/  Bukkit-specific helpers: MiniMessage, particles, PDC, config-serializable wrappers
bungee/        Standalone BungeeCord plugin + Bungee impls of config/database/command sender
versions/
  adapter/     SPI interfaces for NMS differences (ItemAdapter, AttributeAdapter, ...)
  v1_20_R4/ v1_21_R1/ v1_21_R3/ v1_21_R5/ v1_21_r7/   Per-Minecraft-version implementations
.github/workflows/build-and-publish.yml   the only CI workflow
```

## 3. Build, test, publish

```bash
./gradlew build            # compiles all modules, runs tests, produces shadow jars
./gradlew test             # all JUnit tests
./gradlew :<module>:test   # module specific tests
./gradlew publish          # publish to GitHub Packages (needs USERNAME/TOKEN or gpr.user/gpr.key)
```

- Outputs: `core/build/libs/WLib-<version>.jar` (Bukkit) and `bungee/build/libs/WLib-BungeeCord-<version>.jar` (BungeeCord). `assemble` depends on every `shadowJar` and `reobfJar` (paperweight) — root `build.gradle:29-34`.
- The **core jar shades** all sibling modules (utils, commands, config, database, objects, bukkit-utils, versions/*) but **not** gson/Adventure/sqlite — those come from `libraries:` in `plugin.yml` (Bukkit auto-downloads them) or the consumer. The **bungee jar shades** commands/config/database/objects/utils **and bundles** gson + `sqlite-jdbc` + native libs.
- No MySQL/MariaDB driver, no HikariCP, no LuckPerms/PlaceholderAPI dependency anywhere — MySQL support is code-only (`jdbc:mysql://…` string), the driver must be provided by the runtime.
- CI: JDK 21 temurin, `./gradlew build`, uploads `**/build/libs/*.jar`; `publish` job runs for pushes/releases/dispatch (never PRs).

## 4. Module dependency graph

```
objects ──> utils (compileOnly)
config ──> utils, objects
database ──> objects (compileOnly)
commands ──> objects, utils          (+ gson for JsonElementCommandNode)
bukkit-utils ──> utils
versions:adapter ──> utils
versions:v* ──> versions:adapter, utils, bukkit-utils
core ──> utils, objects, config, database, commands, bukkit-utils, versions:adapter, versions:*  (all implementation)
bungee ──> commands, config, database, objects, utils   (no Bukkit at all — Bukkit-free jar)
```

`commands`, `config`, `database` are deliberately **platform-neutral**; Bukkit glue lives in `core`, BungeeCord glue in `bungee`.

## 5. How it all fits together at runtime

### Bukkit side — `core/src/main/java/com/wizardlybump17/wlib/WLib.java`

`plugin.yml` declares `load: STARTUP`, so most wiring happens in **`onLoad()`** (`WLib.java:55-68`), before any other plugin enables:

1. `gson = new GsonBuilder().create()`.
2. `initCommandSystem()` (`:70-85`): builds `MethodCommandNodeFactoryRegistry` → `MethodCommandExtractor` → `CommandManager` → `WLibCommandExecutor` → `BukkitCommandManagerListener` (added as a `CommandManagerListener`). Registers factory defaults + `Player`, `OfflinePlayer`, `JsonElement` factories (`:74-77`).
3. `ItemMetaHandlerModel.initModels()` — registers Skull/Potion/LeatherArmor/Firework meta models (throws if called twice).
4. `initAdapters()` → `setupAdapters()` (`:155-200`): **hardcoded `switch` on `Bukkit.getMinecraftVersion()`** that calls `setInstance(...)` on `ItemAdapter`, `PlayerAdapter`, `AttributeAdapter`, `CommandMapAdapter` with the matching `versions/v1_*` implementation. Unknown version → logs `severe` and falls back to the `v1_21_r7` adapters (`:192-198`, without a CommandMapAdapter).
5. `initSerializables()` (`:135-153`): `ConfigurationSerialization.registerClass(...)` for `ItemBuilder`, `ItemFilter`, `NumberFormatter`, `PaginatedInventoryBuilder`, `InventoryNavigator`, `ItemButton`, `ParticleSpawner`, particle/potion/sound wrappers — so they work inside Bukkit YAML.
6. `DatabaseRegister.getInstance().registerDatabaseModel(new MySQLDatabaseModel() / new SQLiteDatabaseModel())` (`:63-65`).
7. `initConfigs()` (`:87-94`): `ConfigHolderFactoryRegistry.put(WLib.class, new BukkitConfigHolderFactory(this))` and `ConfigHandlerRegistry.register(SaveControllersTask.class)`.

**`onEnable()`** (`:97-104`): registers `EntityListener` (inventory clicks) + `PlayerListener` (join/quit → player controllers), schedules `SaveControllersTask` every `SaveControllersTask.delay` ticks (configurable).
**`onDisable()`** (`:107-113`): clears the command system (registry, manager, listeners, sender cache), unregisters events, cancels the task.
Consumers reach everything via getters: `getMethodCommandExtractor()`, `getCommandManager()`, `getCommandExecutor()`, `getMethodCommandNodeFactoryRegistry()`, `getGson()`, `getInstance()`.

### BungeeCord side — `bungee/.../WLib.java`

Deliberately thin: only `onLoad()` registers the MySQL/SQLite database models globally (`onEnable` does not exist). Everything else in the bungee jar (`BungeeConfig`, `BungeeConfigHolder(Factory)`, `BungeeDatabaseHolder`, `BungeeCommandSender`, `ColorUtil`, `PluginUtil`) is **library API** that the consuming Bungee plugin wires up itself — including registering its factory into `ConfigHolderFactoryRegistry` (the bungee plugin does NOT do it).

## 6. The command system

Location: `commands/src/main/java/com/wizardlybump17/wlib/command/` (platform-agnostic), Bukkit glue in `core/.../command/`, Bungee sender in `bungee/.../command/`. It is inspired by Mojang's Brigadier but driven by **Java annotations on methods** instead of a fluent builder.

### 6.1 Concepts

- **`Command`** — a wrapper around a root `LiteralCommandNode`.
- **`CommandNode<T>`** (`node/CommandNode.java:17`) — one token of a command. Immutable; "modification" methods (`withChildren`, `withExecutor`, `withPermission`) return copies. Each node holds: name, children, `AllowedInputs<T>` (validation), `Suggester<T>` (tab completion), optional `CommandNodeExecutor`, optional permission string, and `parse(String)` (abstract).
  - **Literal nodes** (`LiteralCommandNode`): fixed keywords (`welcome`, `give`, …). Self-configure case-insensitive matching (`AllowedStringInputs.valueIgnoreCase(name)` + `LiteralSuggester.of(name)`).
  - **Argument nodes**: typed placeholders written `<name>` in the annotation. Implementations: `StringCommandNode`, `BooleanCommandNode`, `CharacterCommandNode`, `Byte/Short/Integer/Long/Float/Double/NumberCommandNode`, `UUIDCommandNode`, `EnumCommandNode<E>`, `JsonElementCommandNode`, `InstantCommandNode`, plus core's `PlayerCommandNode` / `OfflinePlayerCommandNode`.
- **`@Command`** (`annotation/Command.java:12-17`) — method annotation with exactly two members:
  - `String value()` — the full command pattern, space-separated, e.g. `"welcome <name> world"`.
  - `String permission() default ""` — applied to **every** node of the generated tree.
  There are **no** aliases/description/requireSender attributes — don't invent them.
- **`@NonNullInput`** — parameter annotation; flips the input validation of that argument from `anyNullable()` to `anyNotNull()` (honored by the String, UUID, JsonElement, Player, OfflinePlayer factories).

### 6.2 Definition flow: annotated methods → node tree

`MethodCommandExtractor.extract(Object holder)` (`extractor/method/MethodCommandExtractor.java:41`):

1. Scan `holder.getClass().getMethods()` for `@Command`.
2. `value().split(" ")` → tokens (`:54`). A token wrapped in `<…>` is an **argument**, anything else a **literal**.
3. Build the tree **right-to-left** (`:59`): starting from the last token/last method parameter (`parameterIndex = parameters.length - 1`), walking backwards, asking `MethodCommandNodeFactoryRegistry` for a factory based on each **method parameter type** (`:83-105`).
4. The executor is attached only to the **deepest** node (`withExecutor(...)` at `:68`).
5. Result: `new Command((LiteralCommandNode) root)` (`:74`).

Example mapping — `@Command("welcome <name> world")` with `void f(CommandSender<?> s, String name)`:

```
"welcome" (literal)
 └─ "name" (StringCommandNode)      ← bound to method param `name`
     └─ "world" (literal)           ← carries the executor
```

**Executor selection** (`MethodCommandExtractor.createExecutor`, `:136-183`): picks one of 10 `AbstractMethodCommandNodeExecutor` subclasses based on (first parameter is `CommandSender`? / is `CommandContext`? / neither) × (return type `CommandResult`? / anything → auto-wrapped into `CommandResult.successful(value)`). Method parameters after the optional sender/context are bound, in order, to the **argument nodes** — literal nodes are skipped (`executor/.../AbstractMethodCommandNodeExecutor.java:153-157`).

### 6.3 Type resolution: factories and their registry

`MethodCommandNodeFactory` (`extractor/method/factory/MethodCommandNodeFactory.java:13-33`):
`create(Object holder, Method, Command, Parameter, String name, CommandNode<?> root)`, `Class<?>[] getSupportedTypes()`, `isSupported(Class)` (strict equality, unless `isStrict()==false` → `isAssignableFrom`, e.g. `EnumMethodCommandNodeFactory` accepts any enum).

`MethodCommandNodeFactoryRegistry` (`registry/MethodCommandNodeFactoryRegistry.java`):
- `addFactory(factory, Class...)` / `addFactory(Class, factory)`; lookup = exact map hit, then `isSupported` scan (`:39-48`).
- `registerDefaults()` (`:74`): Boolean, Character, Number (all primitives), String, UUID, Enum.
- Core adds `Player`, `OfflinePlayer`, `JsonElement` factories (`core/.../WLib.java:74-77`).
- Unknown parameter type → `MethodCommandNodeFactoryNotFoundException`.

**To support a custom argument type**, write a factory that returns the matching `CommandNode` subclass and register it — exactly what core does for `Player` (`core/.../factory/PlayerMethodCommandNodeFactory.java:17`).

### 6.4 Registration

`CommandManager` (`manager/CommandManager.java`):

- `registerCommand(String identifier, Command command, @Nullable Object holder)` (`:51`); full name = `identifier + ':' + name` (`SEPARATOR`, `:28`). Overloads without holder and bulk `registerCommands(...)` (`:67-82`).
- Re-registering the same full name **merges** trees (`Command.merge` → `CommandNode.merge`, `node/CommandNode.java:160`, executor resolution `:189-207`) — so `@Command("shop")` in one class and `@Command("shop buy <item>")` in another coexist.
- Bookkeeping maps: `commandsByFullName`, `commandsByName`, `commandsByHolder`, `holdersByFullName` (`:30-34`).
- `CommandManagerListener` (`manager/listener/CommandManagerListener.java`) callbacks: `onRegister`, `onUnregister`, `onPreClear`, `onPostClear` — this is the extension point platforms use to bind commands.
- `clear()` clears all; `clearListeners()`; `addListener(...)`.

### 6.5 Execution: `CommandManager.execute`

`execute(CommandSender<?> sender, List<String> input)` (`:87`):

1. Look up the command by full name `identifier:name`, then by bare name (`:93-98`).
2. Walk input tokens; at each step try each candidate child: `child.parseOrInvalid(token)` (`CommandNode.java:57-62` = `parse()` then `isValidInput()` via `AllowedInputs`). Successfully parsed argument values are recorded into `CommandContext.CommandNodeArgument(node, input, parsedData)`.
3. Failure modes produce structured errors, not exceptions: `CommandResult.Errors.parseError(...)` (couldn't parse), `.inputError(...)` (parsed but rejected by `AllowedInputs`), `.nodeNotFound(...)`, `.noPermission(...)` (from `lastNode.canExecute(sender)`), `.noCommandExecutor(...)` (`manager :143-163`).
4. The deepest reached node's executor runs `executor.execute(context)`; any thrown exception is wrapped in `CommandExecutionException` with message `"Error while executing the command %s, index %s, node %s"` (`:167-173`).
5. String overloads (`:176-180`) tokenize with `utils/.../StringUtil.parseQuotedStrings` — quoted args (`"two words"`) stay one token.

### 6.6 Validation and tab completion

- **`AllowedInputs<T>`** (`input/AllowedInputs.java:11`): `isAllowed(T)`. Composables: `AllowedListInputs`, `RangedAllowedInputs(from, to)`, `SingleValueInput`. Concrete: `AllowedIntegerInputs.range(from, to)` (requires `from < to`) / `.unlimited()` / `.positive()` / `.negative()` / `.values(List)` / `.value(v)`; `AllowedStringInputs.valuesIgnoreCase(...)` / `.value(...)` / `.anyNotNull()`; `AllowedBooleanInputs.trueValue()/falseValue()/anyNullable()`; `AllowedUUIDInputs.anyNotNull()`.
- **`Suggester<T>`** (`suggestion/Suggester.java:14-21`): `getSuggestions(CommandSender, String input, String current, CommandNode current) throws SuggesterException`, plus `getStringRepresentation(T)` and `needsEscape()` (String suggesters return the current text escaped — see `StringSuggester.java:29-32,53`). Implementations: `LiteralSuggester.of(name)`, `IntegerSuggester.range(1, 10)/unlimited()`, `EnumSuggester.any(Class)`, `ValuesSuggester.of(values, mapper, needsEscape)`, `CachingSuggester.of(suggester, expirationMillis)` (default 30 000 ms, `CachingSuggester.java:14,55`).
- `CommandManager.getSuggestions(...)` (`:184`) walks the tree, filters nodes by permission, and asks each candidate for suggestions. **Prefix filtering is NOT done here** — it's done by the platform (`core/.../WLibCommandExecutor.java:65-68` filters against the current token).

### 6.7 Context, sender, result

- **`CommandContext`**: `(Command, CommandSender<?>, CommandNodeArguments, int lastInputIndex, CommandNode<?> lastNode)`. Arguments keyed by **node name** (`getArgument(name)`, `getArgumentData(name)`, `hasArgument(name)`).
- **`CommandSender<S>`** (`sender/CommandSender.java`): `getHandle()`, 4 `sendMessage` overloads, `getName()`, `hasPermission(String)`, `hasId()`, `getId()` (throws `IllegalStateException` when the sender has no UUID). Platform impls: core's `BukkitCommandSender` (with a UUID→sender cache, `CONSOLE` singleton, `from(...)` factory) and `BungeeCommandSender`. For platform-free code/tests: `BasicCommandSender` + its `Builder` (defaults: print to `System.out`, allow every permission).
- **`CommandResult<T>`** (`result/CommandResult.java`): immutable `data/type/resultCode/message` with copy-style `with...`-equivalents (`data()`, `type()`, `resultCode()`, `message()` return new instances). `Type` enum mirrors HTTP-ish semantics: `SUCCESS, NO_CONTENT, BAD_REQUEST, CONFLICT, FORBIDDEN, GENERIC_ERROR, INVALID_SENDER, NOT_FOUND, UNAUTHORIZED, UNPROCESSABLE_CONTENT, NOT_IMPLEMENTED`; `isSuccess()` is true only for `SUCCESS`/`NO_CONTENT`. Statics: `CommandResult.successful(obj)`, `.noContent()`, `.badRequest()`, …; codes from `CommandResultCodes` (e.g. `"WLib:ParseError"`). Manager-generated failures are `CommandResult.Errors.*`.

### 6.8 Bukkit integration (`core/.../command/`)

- `WLibCommandExecutor` (`command/WLibCommandExecutor.java:18`) implements Bukkit's `CommandExecutor` + `TabCompleter`: wraps the Bukkit sender via `BukkitCommandSender.from(sender)` and delegates to `CommandManager.execute/getSuggestions`; logs `CommandExecutionException`/`SuggesterException`.
- `BukkitCommandManagerListener` (`command/listener/BukkitCommandManagerListener.java:27-45`) binds every registration to Bukkit:
  - holder is a `JavaPlugin` **and** command is a `BukkitCommand` (extends the neutral `Command`, wraps `InternalBukkitCommand`, a real Bukkit `Command`): registered via `Bukkit.getCommandMap().register(name, identifier, internalCommand)` — **no `plugin.yml` entry needed**.
  - otherwise (plain extracted `Command`): looks up `plugin.getCommand(command.getName())` and sets `WLibCommandExecutor` as its executor — **the name must be declared in the consumer's `plugin.yml`**, else a warning is logged and the command is skipped (`:38-41`).
  - `onUnregister`/`onPreClear` remove the command from `CommandMap.getKnownCommands()` or null out the executor (`:61-77`).
- Player-typed arguments: `PlayerCommandNode`/`OfflinePlayerCommandNode` parse via `Bukkit.getPlayer(...)` / offline lookup; suggesters `PlayerSuggester.Online`, `OfflinePlayerSuggester`; inputs `AllowedPlayerInputs`/`AllowedOfflinePlayerInputs`.

### 6.9 Examples (copied from the actual tests / wiring)

**Define + execute (platform-agnostic)** — see `commands/src/test/.../MethodCommandExtractorTests.java`:

```java
class MyCommands {
    @Command("welcome <name>")
    public void welcome(@NotNull CommandSender<?> sender, @NotNull String name) {
        sender.sendMessage("Welcome " + name);
    }

    @Command("welcome <name> world")            // deeper literal keeps the executor
    public CommandResult<?> welcomeWorld(@NotNull CommandSender<?> sender, @NotNull String name) {
        return CommandResult.successful("Welcome " + name);
    }

    @Command("aye <name> nice")
    public CommandResult<?> ayeNice(@NotNull String name) {     // sender optional
        return CommandResult.successful("Aye " + name);
    }
}

MethodCommandNodeFactoryRegistry registry = new MethodCommandNodeFactoryRegistry();
registry.registerDefaults();
MethodCommandExtractor extractor = new MethodCommandExtractor(registry);
CommandManager manager = new CommandManager();
manager.registerCommands("test", extractor.extract(new MyCommands()));

manager.execute(sender, "welcome Chad world");   // -> CommandResult.successful("Welcome Chad")
manager.execute(sender, "aye Chad nice");        // -> CommandResult.successful("Aye Chad")
```

**Manual node tree with validation** — `CommandExecutionTests.java`:

```java
IntegerCommandNode worldNode = new IntegerCommandNode(
        "world", List.of(),
        AllowedIntegerInputs.value(10),                 // only 10 is valid
        null,
        context -> CommandResult.successful(10),
        null);
commandManager.execute(sender, List.of("hello", "world"));
// "world" is not an int -> CommandResult.Errors.parseError("world", "world", 1, InputParsingException(...))
```

**Tab completion** — `SuggestionTests.java`:

```java
new UUIDCommandNode("world", List.of(), AllowedUUIDInputs.anyNotNull(),
        UUIDSuggester.values(UUID.nameUUIDFromBytes("WizardlyBump17".getBytes())), null, null); // parent: "hi"
new IntegerCommandNode("there", List.of(), AllowedIntegerInputs.unlimited(),
        IntegerSuggester.range(1, 10), executor, null); // parent: "hello"
commandManager.getSuggestions(sender, List.of("hello", ""));   // proposes the UUID values / 1..10
```

**Registering a command from a Bukkit plugin** (typical consumer flow):

```java
// in your JavaPlugin#onEnable, with depend: [WLib]
WLib wlib = WLib.getInstance();
List<Command> commands = wlib.getMethodCommandExtractor().extract(new MyShopCommands());
wlib.getCommandManager().registerCommands("myplugin", commands, this);
// BukkitCommandManagerListener then points your plugin.yml command at WLibCommandExecutor
```

**Custom argument type**: implement `MethodCommandNodeFactory` returning your `CommandNode` subclass, then `wlib.getMethodCommandNodeFactoryRegistry().addFactory(new MyFactory())` — same mechanism core uses for `Player`/`OfflinePlayer`/`JsonElement` (`core/.../WLib.java:74-77`).

## 7. Modules in detail

### 7.1 `commands`
The command framework — fully covered in §6. Build: `build.gradle.kts` (the only Kotlin-DSL module); deps: `objects`, `utils`, gson 2.13.2 (only for `JsonElementCommandNode`), jetbrains annotations. No Bukkit/Bungee imports. Tests here are the specification — if you change behavior, update these tests.

### 7.2 `core` (Bukkit plugin)
- **Lifecycle** — see §5. Resources: `plugin.yml` (`load: STARTUP`, `api-version: 1.13`, `libraries:` supplies Adventure 4.18/MiniMessage/adventure-platform/gson at runtime), `configs/save-controllers.yml` (an **empty** file; real defaults come from annotations).
- **`command/`** — Bukkit glue for the command framework (§6.8) + Player/OfflinePlayer node/factory/suggester/input classes.
- **`inventory/`** — GUI framework:
  - `PaginatedInventoryBuilder` (`paginated/PaginatedInventoryBuilder.java:30`, `@SerializableAs`) → fluent `title/shape/shapeReplacement/content/nextPage/previousPage/listener/initialData/data`, `build()` (`:238`) splits content into pages → `PaginatedInventory` (`show(HumanEntity, page)`, `showNextPage/showPreviousPage`, `setData/hasData/getData/removeData`, `startListeners/stopListeners`).
  - `CustomInventory` maps shape characters to slots (`indexOf(char)`); `CustomInventoryHolder` links a Bukkit `InventoryHolder` back to the `PaginatedInventory`.
  - `ItemButton` (`Supplier<ItemStack>` + `ClickAction` + `customData` + click `ConfigSound`s), `ClickAction = void onClick(InventoryClickEvent, PaginatedInventory)`, `InventoryNavigator.NEXT_PAGE/PREVIOUS_PAGE` standard buttons.
  - Click dispatch lives in `listener/EntityListener.java:22-68`: cancels clicks on WLib inventories, resolves `ItemButton` for the raw slot, invokes `ClickAction`, plays click sounds, cancels drags, stops listeners on close.
  - `InventoryListener` (`@Builder`, plugin + event class + priority + `ListenerConsumer<T>`) for custom per-inventory event hooks.
- **`item/`** — `ItemBuilder` (`item/ItemBuilder.java:41`, `@SerializableAs`, cloneable): fluent `type/amount/damage/lore/displayName/itemFlags/unbreakable/customModelData/enchantments/nbtTags/attributes/customData`, meta helpers (`consumeMeta`, `getFromMeta`), `build()`, `fromItemStack`; delegates NMS-sensitive parts to `ItemAdapter`/`AttributeAdapter` from `versions:adapter`. `ItemFilter` + `FilterType` enum (`MATERIAL, AMOUNT, DAMAGE, DISPLAY_NAME, LORE, ENCHANTMENTS, ITEM_FLAGS, GLOW, UNBREAKABLE`) with `testAny/testAll/accept/first`. `ItemMetaHandlerModel` static registry maps `Material` → concrete meta handler (Skull/Potion/LeatherArmor/Firework).
- **`controller/`** — Bukkit glue for the `database` Controller pattern: `ControllerEventListener` (a `ControllerListener` + Bukkit `Listener` with `listen(E)`/`getEventClass()`/`getPriority()`), `ControllerEventListenerBuilder`, `ControllerListenerRegisterer` (registers each listener via `PluginManager.registerEvent`), `UnloadReasons.QUIT/UNKNOWN`.
- **`task/SaveControllersTask`** — `BukkitRunnable` bound to config via `@ConfigInfo(name="configs/save-controllers.yml", holderType=WLib.class)`; static `@Path("log") boolean log`, `@Path("delay") int delay = 5*60*20` ticks; `run()` calls `controller.save()` for every controller in `ControllerCache.INSTANCE`.
- **`listener/PlayerListener`** — on join calls `loadPlayer(uuid)` and on quit `unloadPlayer(uuid, UnloadReasons.QUIT)` on every cached `PlayerController` (futures are fire-and-forget, not joined). `EntityListener` additionally drops players from `BukkitCommandSender`'s cache on `EntityRemoveFromWorldEvent`.
- **`config/`** — `Config extends YamlConfiguration implements Configuration<YamlConfiguration>` with extras (`getItemBuilder`, `getNumberFormatter`, `getEnumMap`, `getByType`, `getFancyString`); `Path` options honored here: `"fancy"` colorizes strings via `bukkit-utils` `StringUtil.fancy`, `fixNumber` coerces numbers, sections → `Map`, `ConfigWrapper.unwrap()`. `BukkitConfigHolder(Factory)` implement the `config` module SPI per-plugin.
- **`util/`** — `ItemUtil` (Base64 `ItemStack`/inventory serialization), `BukkitStreamsUtil`, `InventoryUtil` (`canFit`, `canRemoveAll`, …), `NumberFormatter` (extends the utils one, adds serializability), `ThreadUtil.runSync/runAsync`, `particle/ItemStackWrapper` (a `ConfigWrapper<ItemBuilder>` living in core despite the `util.bukkit.particle` package name — same package name as bukkit-utils' particle classes, different module).
- `database/BukkitDatabaseHolder` exists but is **not used inside the repo** — it's an SPI convenience for consumers.

### 7.3 `database`
Async-on-top-of-sync design:
- `Database<M extends DatabaseModel<?>>` (`Database.java:17`): **one shared `java.sql.Connection`**, no pooling, no reconnect; `open()/close()`, `update(sql, params)`, `returnUpdate(...)` (generated keys), `query(...)` (caller must close the `ResultSet`). All synchronous; errors are **logged, not rethrown** (there's a `TODO` about this at `:16`).
- ORM-ish layer: `save(DatabaseStorable, table)` (`:125-141`) dispatches to delete (`isDeleted()`), update (`isInDatabase() && isDirty()`) or insert. SQL is built from the storable's `Map<column, value>` hooks (`DatabaseStorable.saveToDatabase/updateToDatabase(…, where)/deleteFromDatabase`); empty map ⇒ statement skipped.
- `DatabaseModel<D>`: `type` id + JDBC URL template. `MySQLDatabaseModel` → `jdbc:mysql://{host}:{port}/{database}` (localhost/3306 defaults); `SQLiteDatabaseModel` → `jdbc:sqlite:<absolute file>` (defaults `database.db` in the holder's data folder; `SQLiteDatabase.open` creates the file/dirs and `Class.forName("org.sqlite.JDBC")`).
- **Dialect portability trick**: you can write either `AUTO_INCREMENT` or `AUTOINCREMENT` — `MySQLDatabase` and `SQLiteDatabase` each rewrite the other's keyword (and the models translate `Modifier` enums for DDL).
- **`DatabaseRegister`** (singleton): registry of models by lowercase `type`; `createDatabase(String propertiesFile | Properties, DatabaseHolder)` — a `.properties` file in the holder's data folder with a `type=sqlite|mysql` key selects the model. Registered models come from core/bungee `onLoad`. Failures `printStackTrace()` and return `null`.
- **Controller pattern** (the reason `objects/Cache` and the async API exist): `Controller<K, V, C extends Cache<K,V,?>, D extends DAO<K,V>>` wraps a cache + DAO: `get(key)` → cache hit or `dao.find(key)` (async) then caches it; `save()` flushes every cached value via `dao.update`; `shutdown()` = `save().join()` (note: documented as **not called natively**). `ControllerCache.INSTANCE` is a global registry of controllers (not a data cache) that core iterates for join/quit/periodic-save fan-out.
- `PlayerController` (`controller/player/PlayerController.java`): abstract `CompletableFuture<Void> loadPlayer(UUID)` / `unloadPlayer(UUID, UnloadReason)`. Net behavior: hot player data lives in the cache while online; loaded async on join; written back on quit and every 5 min (`SaveControllersTask`).
- `DAO<K,V>` (`dao/DAO.java`): async repository surface — `find(K)`, `update(V)`, `fetchAll()`, `updateAll(...)`, and `setupDatabase()` where **you** create the tables.
- **Important gap**: the annotations `@Table`, `@Column`, `@Exclude`, `@Modifiers`, `DatabaseRegister`... are NOT consumed anywhere — nothing reads `getDeclaredFields` for DB purposes, no `CREATE TABLE` is generated, `getModifierCommand` is never called. Annotation→table generation is an unfinished extension point; DDL is manual, in `DAO#setupDatabase`.

### 7.4 `config`
Reflection binding of static fields to YAML-like config files:
- `@ConfigInfo(name, holderType, saveDefault=false)` on a class; `@Path("some.path", options={...})` on its **static** fields.
- `ConfigHandlerRegistry.register(Class)` (`ConfigHandlerRegistry.java:24`): reads `@ConfigInfo` → looks up a `ConfigHolderFactory` by `holderType` in `ConfigHolderFactoryRegistry` (throws `NullPointerException("ConfigHolderFactory not found for ...")` if missing) → factory creates a `ConfigHolder` → `holder.getConfig(name)` → `ConfigHandler.init()` writes missing defaults from the field's current value, `loadFields()` pushes file values into the static fields (`field.set(null, ...)`), then the handler is stored. `reload()`/`save()`/bulk `reloadAll/initAll/saveAll/unregisterAll/removeAll(holder)`.
- Platform-neutral `Configuration<H>` interface (`get/set/saveConfig/reloadConfig/isSet/getByType/getHandle`, overloads that receive the requesting `Path`); `ConfigHolder<H, C>`/`ConfigHolderFactory` are implemented per platform (Bukkit: core; Bungee: bungee). There is **no ServiceLoader/SPI** — factories must be explicitly `put(...)` into the registry.
- Custom value types hook in via `bukkit-utils` `ConfigWrapper<T>` (unwrapped by core's `Config.java:242`) or by registering `ConfigurationSerializable` classes.

### 7.5 `objects`
Four tiny primitives: `Pair`/`Triple` (Lombok `@Data` tuples with `map(...)` transforms), abstract `Cache<K,V,T>` (map populated through `apply(T) → Pair<K,V>`; `get→Optional`, `getOrInsert`, `add/addAll`, `getAll`, `remove`, `has`), abstract `Registry<K,V>` (`get/put/has/remove/clear/getKeys/getValues` — base of the config/database registries). Note: `utils` is a `compileOnly` dep here.

### 7.6 `utils`
Pure static helpers, no runtime deps. Frequently used ones: `StringUtil` (`applyPlaceholders` with `{}`/`\\` defaults → `PlaceholderException`; `parseQuotedStrings` — the command-line tokenizer used by `CommandManager`, → `QuotedStringException` with `UNCLOSED_QUOTE`/`INVALID_ESCAPE`; `escapeString`, `pascalToCamel`, `getName(Enum)`, `fixName`), `CollectionUtil` (`breakLines` word-wrap, `join`, `listOf`, `deepClone`, `sort`), `MapUtils` (`mapOf`, `dotKeys`/`dotToMap` flatten/unflatten `a.b.c` keys, `sortByValues`, …), `NumberUtil` (nullable numeric getters, Roman numerals), `NumberFormatter` (compact format with suffixes, e.g. `1.5K`, plus inverse parse), `DateUtil` (`toDuration("1d2h")`, `fromDate`, `getDifference`), `TimeUnit` (millis constants), `RandomUtil`, `ReflectionUtil` (get/set field, invoke method with a Logger), `FileUtil`, `ArrayUtils`, `builder/MapBuilder` (fluent `putIf*` family), `ObjectUtil.clone`. Test: `QuotedStringsTests` covers the tokenizer.

### 7.7 `bukkit-utils`
All deps `compileOnly` (Paper 1.20.6, Adventure/MiniMessage 4.18, adventure-platform 4.3.4):
- `ConfigUtil` — null-safe map access (`get/map/mapOrDefault` with `@Contract`), the backbone of every `deserialize` in the project.
- `MiniMessageUtil` — MiniMessage parsing with tag-resolver placeholders (`getMessage(msg, key, value, ...)`), keys normalized via `pascalToCamel`.
- `StringUtil` (Bukkit flavor) — legacy `&`/hex `<#RRGGBB>` colorizing (`fancy` also converts literal `\n`), `toBungeeColor`, coordinate `toString`.
- `NamespacedKeyUtil` (`fromString` lenient parse), `PersistentDataContainerUtil` (`containerOf(Map<NamespacedKey,Object>)` with the standard PDC types), `persistence/EmptyDataType` (flag-only PDC keys).
- `collector/ComponentCollector` — a `Collector<Component,Component[],Component>` with `COMMA`/`NEW_LINE`/`EMPTY` separators; used by senders to join message parts.
- Config-serializable wrappers (`@SerializableAs("WLib:...")`, registered by core at startup): `ConfigSound` (`play(Player/Location)`), `ConfigWrapper<T>` interface (`T unwrap()`), potion wrappers, particle classes: `ParticleSpawner` (full particle spec + `spawn/spawnExact/spawnRotating`), `BlockDataWrapper`, `DustOptionsWrapper`, `DustTransitionWrapper`.

### 7.8 `bungee`
Standalone Bukkit-free plugin (descriptor is `bungee.yml`, not plugin.yml). Provides Bungee implementations of the neutral abstractions: `BungeeConfig` (implements `Configuration<net.md_5.bungee.config.Configuration>`, auto-colorizes strings via `ColorUtil` `&`→`§` + `<hex>`→`§x`, `fixNumber`), `BungeeConfigHolder(Factory)` (factory scans `ProxyServer` plugins when no explicit plugin given), `BungeeDatabaseHolder`, `BungeeCommandSender` (legacy `&` messages, `getId()` only for `ProxiedPlayer`), `PluginUtil.saveResource`, `ComponentCollector` (BaseComponent flavor). `onLoad` registers the two database models — nothing else; consumers wire the rest themselves (including config-factory registration). Shades gson + sqlite-jdbc into its jar.

### 7.9 `versions/`
- `versions/adapter` (SPI, compiled against paper-api 1.20.6): `Adapter` (`checkIsOnRightVersion()` vs `getMinecraftVersions()`), `ItemAdapter` (item damage, custom data, PDC transfer, NBT), `AttributeAdapter` (attribute name↔`Attribute` mapping), `command/CommandMapAdapter` (reflection access to `CraftBukkitCommandMap.knownCommands`), `player/PlayerAdapter` (CraftBukkit conversation queue). All are static singletons `setInstance()/getInstance()`, set exactly once by core's `setupAdapters()` switch (§5). There is **no NMS package sniffing** — the version→adapter mapping is a hardcoded switch; version strings are duplicated between each `Adapter.getMinecraftVersions()` and the WLib switch.
- Per-version modules compile against their own paper dev bundle (`v1_20_R4`→1.20.5-1.20.6, `v1_21_R1`→1.21-1.21.1, `v1_21_R3`→1.21.4, `v1_21_R5`→1.21.6-1.21.7, `v1_21_r7`→1.21.11 bundle) and are shaded into the core jar. Mapping used by the switch: 1.20.5-1.20.6 → `v1_20_R4`; 1.21-1.21.1 → `v1_21_R1`; 1.21.4 → `v1_21_R3`; 1.21.6-1.21.7 → `v1_21_R5`; 1.21.11 **and unknown versions** → `v1_21_r7`.
- **Adding a new Minecraft version** = 4 steps: (1) create `versions/v*/` with `build.gradle.kts` + `BaseAdapter`/`ItemAdapter`/`AttributeAdapter`/`player.PlayerAdapter`/`command.CommandMapAdapter`; (2) `include 'versions:vx'` in `settings.gradle` (rename to lowercase via `findProject(...)?.name` if the folder is capitalized); (3) add `implementation(project(":versions:vx"))` to `core/build.gradle`; (4) add a `case` to `WLib.setupAdapters()`; (5) change the default block to use the new version, if it is more recent than the last known.

## 8. Conventions

- Packages: everything under `com.wizardlybump17.wlib.*`; module boundaries match package sub-trees (`wlib.command`, `wlib.config`, `wlib.database`, `wlib.object`, `wlib.util`, `wlib.util.bukkit`, `wlib.inventory`, `wlib.item`, `wlib.adapter`).
- Fluent builders/builder-style copies are the dominant API idiom (`ItemBuilder`, `PaginatedInventoryBuilder`, `BasicCommandSender.Builder`, `ControllerEventListenerBuilder`, `MapBuilder`, `CommandResult` copy methods).
- Immutable-by-copy nodes in the command tree (`withChildren`/`withExecutor`/`withPermission` return new instances).
- Serialization classes are YAML-round-trippable: `@SerializableAs("WLib:...")` + `serialize`/`deserialize` via `ConfigUtil`, registered centrally in `WLib.initSerializables()`.
- Errors: prefer `CommandResult.Errors.*` / `Optional` / `@Nullable` returns over exceptions in library code (exceptions are reserved for misuse like `MethodCommandNodeFactoryNotFoundException`).
- Style of new version adapters: copy an existing `versions/v*` module and keep class names identical across versions.

## 9. AGENTS.md

When writing code check, in the end, if AGENTS.md needs changes.
