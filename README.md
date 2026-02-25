# judo-dispatcher-api

[![Build](https://github.com/BlackBeltTechnology/judo-dispatcher-api/actions/workflows/build.yml/badge.svg?branch=develop)](https://github.com/BlackBeltTechnology/judo-dispatcher-api/actions/workflows/build.yml)

## Introduction

The Dispatcher API defines the core contracts for routing incoming API calls to the appropriate action in the JUDO-NG platform. Actions can be recursive Dispatcher calls (for nested payloads) or direct database calls. The Dispatcher also handles mapping and validation of inputs before forwarding them.

This is a **pure API library** — it contains only interfaces, annotations, and lightweight model classes with no implementation code. Implementations live in other JUDO-NG modules that depend on this API.

## API Overview

All types live in the `hu.blackbelt.judo.dispatcher.api` package:

```mermaid
classDiagram
    class Dispatcher {
        <<interface>>
        +callOperation(String operationFQN, Map exchange) Map
        +coerce(S sourceValue, Class~T~ targetClass) T
        +coerce(S sourceValue, String targetClassName) T
    }

    class Context {
        <<interface>>
        +get(String key) Object
        +getAs(Class~T~ clazz, String key) T
        +put(String key, Object value)
        +putIfAbsent(String key, T value) T
        +remove(String key) Object
        +removeAll()
    }

    class Sequence~T~ {
        <<interface>>
        +getNextValue(String sequenceName) T
        +getCurrentValue(String sequenceName) T
    }

    class VariableResolver {
        <<interface>>
        +resolve(Class~T~ type, String category, String key) T
    }

    class JudoPrincipal {
        -String name
        -String realm
        -String client
        -Map attributes
        +getName() String
    }

    class BusinessException {
        -String type
        -String errorCode
        -Map details
        -Throwable throwable
        -Locale locale
    }

    class FileType {
        -String id
        -String fileName
        -Long size
        -String mimeType
    }

    class JudoOperation {
        <<annotation>>
        +value() String
    }

    JudoPrincipal ..|> Principal : implements
    BusinessException --|> RuntimeException : extends
    Dispatcher ..> Context : uses
    Dispatcher ..> JudoPrincipal : reads from exchange
    Dispatcher ..> BusinessException : throws
```

| Type | Kind | Purpose |
|------|------|---------|
| `Dispatcher` | Interface | Routes API calls to operations; performs type coercion |
| `Context` | Interface | Thread-safe key-value store for passing state through dispatch chains |
| `Sequence<T>` | Interface | Generates sequential values (counters, IDs) by name |
| `VariableResolver` | Interface | Resolves environment/category variables at runtime |
| `JudoPrincipal` | Class | Security principal carrying user name, realm, client, and attributes |
| `BusinessException` | Class | Domain exception with type, error code, details map, and locale |
| `FileType` | Class | File metadata container (ID, filename, size, MIME type) |
| `JudoOperation` | Annotation | Marks methods as Dispatcher operations (runtime-retained) |

### Dispatcher Exchange Model

The `Dispatcher.callOperation()` method uses a `Map<String, Object>` exchange for both request and response. The exchange carries well-known keys as constants on the `Dispatcher` interface:

| Key | Constant | Description |
|-----|----------|-------------|
| `__this` | `INSTANCE_KEY_OF_BOUND_OPERATION` | Mapped transfer object for bound operations |
| `__entityType` | `ENTITY_TYPE_MAP_KEY` | Entity type discriminator |
| `__principal` | `PRINCIPAL_KEY` | Security principal (`JudoPrincipal`) |
| `__actor` | `ACTOR_KEY` | Actor reference |
| `__variables` | `VARIABLES_KEY` | Resolved variables |
| `__headers` | `HEADERS_KEY` | HTTP headers |

### Runtime Flow

```mermaid
sequenceDiagram
    participant Client
    participant Dispatcher
    participant Context
    participant Backend as Backend Service

    Client->>Dispatcher: callOperation(operationFQN, exchange)
    Dispatcher->>Context: get/put state
    Dispatcher->>Dispatcher: coerce input types
    Dispatcher->>Backend: delegate to implementation
    Backend-->>Dispatcher: result
    Dispatcher-->>Client: response exchange
    Note over Client,Dispatcher: On domain error, throws BusinessException
```

## External Dependencies

```mermaid
graph LR
    subgraph "JUDO Dispatcher API"
        Dispatcher[Dispatcher Interface]
        Models[Model Classes]
    end

    subgraph "External Libraries"
        EMF[Eclipse EMF<br/>EPackage/EClass types]
        Lombok[Lombok<br/>Builder/Getter codegen]
        Guava[Google Guava<br/>Utility collections]
        OSGi[OSGi Framework<br/>Bundle packaging]
        SLF4J[SLF4J<br/>Logging facade]
    end

    Dispatcher --> EMF
    Models --> Lombok
    Dispatcher --> Guava
    Dispatcher --> OSGi
    Dispatcher --> SLF4J
```

## Building

This project uses Maven with the Maven Wrapper for reproducible builds:

```bash
# Run tests
./mvnw clean test

# Full build and install to local repository
./mvnw clean install

# Run a single test class
./mvnw test -Dtest=MyTestClass

# Run a single test method
./mvnw test -Dtest=MyTestClass#myMethod
```

> **Note:** Requires Java 21 JDK. The Maven Wrapper (`./mvnw`) ensures the correct Maven 3.9.4 version is used automatically.

### Build Lifecycle

```mermaid
flowchart LR
    compile[Compile<br/>Java 21] --> bundle[Bundle<br/>OSGi metadata]
    bundle --> test[Test<br/>JUnit 5 + JaCoCo]
    test --> package[Package<br/>JAR + Sources + Javadoc]
    package --> install[Install<br/>Local repo]
    package -->|profile: sign-artifacts| sign[Sign<br/>GPG artifacts]
    install -->|profile: release-judong| nexus[Deploy<br/>JUDO Nexus]
    install -->|profile: release-central| central[Deploy<br/>Maven Central]
```

### Maven Profiles

| Profile | Purpose |
|---------|---------|
| `sign-artifacts` | GPG-signs build artifacts for release |
| `release-dummy` | Deploys to local `/tmp/` directory for testing |
| `release-judong` | Deploys to JUDO-NG Nexus snapshot repository |
| `release-central` | Deploys to Maven Central via Sonatype OSSRH |
| `generate-github-asciidoc-diagrams` | Renders AsciiDoc diagrams to PNG for GitHub |
| `update-source-code-license` | Updates EPL 2.0 license headers in source files |

## Context

This project is a building block of the [judo-community](https://github.com/BlackBeltTechnology/judo-community) aggregator project. Check the corresponding documentation there for how this module fits into the broader JUDO-NG ecosystem.

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for details on submitting issues and pull requests.

## License

This project is licensed under the [Eclipse Public License - v 2.0](https://www.eclipse.org/legal/epl-2.0/).
