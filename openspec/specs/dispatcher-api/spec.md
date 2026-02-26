# dispatcher-api Specification

## Purpose

Defines the core API contracts for the JUDO Dispatcher component: operation routing, type coercion, context management, sequence generation, variable resolution, security principal representation, domain exceptions, and file metadata.

## Architecture

All types reside in `hu.blackbelt.judo.dispatcher.api`. The central interface is `Dispatcher`, which routes API calls via a `Map<String, Object>` exchange model. Supporting interfaces (`Context`, `Sequence<T>`, `VariableResolver`) provide runtime services. Model classes (`JudoPrincipal`, `BusinessException`, `FileType`) carry data. The `@JudoOperation` annotation marks methods as dispatchable operations.

## Requirements

### Requirement: Operation dispatching

The `Dispatcher` interface SHALL route API calls to the appropriate action by fully qualified operation name.

#### Scenario: Successful operation call
- **GIVEN** a registered operation with fully qualified name `com.example.MyService#doSomething`
- **WHEN** `callOperation("com.example.MyService#doSomething", exchange)` is invoked
- **THEN** the dispatcher routes to the correct implementation and returns a response exchange

#### Scenario: Bound operation with instance
- **GIVEN** a bound operation requiring an instance
- **WHEN** `callOperation` is invoked with an exchange containing the instance ID
- **THEN** the dispatcher populates the `__this` key with the mapped transfer object before delegating

### Requirement: Type coercion

The `Dispatcher` interface SHALL provide type coercion from a source value to a target type.

#### Scenario: Coerce by target class
- **WHEN** `coerce(sourceValue, TargetClass.class)` is invoked
- **THEN** the source value is converted to an instance of `TargetClass`

#### Scenario: Coerce by target class name
- **WHEN** `coerce(sourceValue, "com.example.TargetClass")` is invoked
- **THEN** the source value is converted to the type identified by the class name string

### Requirement: Exchange well-known keys

The `Dispatcher` interface SHALL define constant keys for the exchange map to ensure consistent access to standard payload entries.

#### Scenario: Standard exchange keys are accessible
- **WHEN** a dispatcher implementation processes an exchange
- **THEN** the following keys are available as constants: `INSTANCE_KEY_OF_BOUND_OPERATION` (`__this`), `ENTITY_TYPE_MAP_KEY` (`__entityType`), `PRINCIPAL_KEY` (`__principal`), `ACTOR_KEY` (`__actor`), `VARIABLES_KEY` (`__variables`), `HEADERS_KEY` (`__headers`)

### Requirement: Thread-safe context management

The `Context` interface SHALL provide thread-safe key-value storage for passing state through dispatch chains.

#### Scenario: Store and retrieve a value
- **GIVEN** an empty context
- **WHEN** `put("key", value)` is called followed by `get("key")`
- **THEN** the stored value is returned

#### Scenario: Type-safe retrieval
- **GIVEN** a context containing a value for key `"count"`
- **WHEN** `getAs(Integer.class, "count")` is called
- **THEN** the value is returned cast to `Integer`

#### Scenario: Conditional put
- **GIVEN** a context where key `"key"` already has a value
- **WHEN** `putIfAbsent("key", newValue)` is called
- **THEN** the existing value is retained and returned

#### Scenario: Remove all entries
- **GIVEN** a context with multiple entries
- **WHEN** `removeAll()` is called
- **THEN** all entries are cleared

### Requirement: Sequence value generation

The `Sequence<T>` interface SHALL generate sequential values identified by name.

#### Scenario: Get next sequence value
- **GIVEN** a sequence named `"orderNumber"`
- **WHEN** `getNextValue("orderNumber")` is called
- **THEN** the next value in the sequence is returned

#### Scenario: Get current value (optional)
- **GIVEN** a sequence implementation that supports current value retrieval
- **WHEN** `getCurrentValue("orderNumber")` is called
- **THEN** the current value is returned without advancing the sequence

#### Scenario: Get current value (unsupported)
- **GIVEN** a sequence implementation that does not override `getCurrentValue`
- **WHEN** `getCurrentValue("orderNumber")` is called
- **THEN** an `UnsupportedOperationException` is thrown

### Requirement: Environment variable resolution

The `VariableResolver` interface SHALL resolve typed environment variables by category and key.

#### Scenario: Resolve a string variable
- **GIVEN** a variable with category `"system"` and key `"timezone"`
- **WHEN** `resolve(String.class, "system", "timezone")` is called
- **THEN** the resolved string value is returned

### Requirement: Security principal representation

The `JudoPrincipal` class SHALL implement `java.security.Principal` and carry user identity and metadata.

#### Scenario: Build a principal
- **WHEN** a `JudoPrincipal` is built with name `"user1"`, realm `"default"`, client `"web-app"`, and attributes `{role: "admin"}`
- **THEN** `getName()` returns `"user1"`, and all fields are accessible via getters

#### Scenario: Name is required
- **WHEN** a `JudoPrincipal` is built without a name
- **THEN** a `NullPointerException` is thrown (enforced by Lombok `@NonNull`)

### Requirement: Domain exception with structured details

The `BusinessException` class SHALL extend `RuntimeException` and carry structured error information.

#### Scenario: Throw a business exception
- **GIVEN** a domain rule violation
- **WHEN** a `BusinessException` is thrown with type `"VALIDATION"`, errorCode `"FIELD_REQUIRED"`, details `{field: "name"}`, and locale `Locale.ENGLISH`
- **THEN** all fields are accessible via getters and the exception propagates as an unchecked exception

### Requirement: File metadata representation

The `FileType` class SHALL carry file metadata using the Builder pattern.

#### Scenario: Build file metadata
- **WHEN** a `FileType` is built with id `"abc123"`, fileName `"report.pdf"`, size `1024L`, mimeType `"application/pdf"`
- **THEN** all fields are accessible via getters

### Requirement: Operation annotation

The `@JudoOperation` annotation SHALL mark methods as dispatchable operations with a fully qualified name.

#### Scenario: Annotate a method
- **GIVEN** a method annotated with `@JudoOperation("com.example.MyService#list")`
- **WHEN** the annotation is inspected at runtime
- **THEN** `value()` returns `"com.example.MyService#list"` and the annotation is present via reflection (`RetentionPolicy.RUNTIME`)
