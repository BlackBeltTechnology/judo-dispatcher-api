# JUDO Dispatcher API - Project Documentation

## Project Overview

**Repository:** BlackBeltTechnology/judo-dispatcher-api
**License:** Eclipse Public License 2.0 (EPL-2.0)
**Java Version:** 21
**Build System:** Maven 3.9.4 with Maven Wrapper (`./mvnw`)

1. Defines the core API contracts for the JUDO Dispatcher component, which routes incoming API calls to appropriate actions (recursive dispatch calls or direct database operations)
2. Provides interfaces for operation dispatching (`Dispatcher`), thread-safe context management (`Context`), sequence generation (`Sequence`), and environment variable resolution (`VariableResolver`)
3. Includes lightweight model classes for security principals (`JudoPrincipal`), domain exceptions (`BusinessException`), file metadata (`FileType`), and an operation annotation (`JudoOperation`)
4. Packaged as an OSGi bundle for deployment in the JUDO-NG runtime platform
5. Part of the [judo-community](https://github.com/BlackBeltTechnology/judo-community) ecosystem

## Directory Structure

```
judo-dispatcher-api/
├── src/
│   ├── main/java/hu/blackbelt/judo/dispatcher/api/   # Public API (8 files)
│   └── test/java/                                      # JUnit 5 tests
├── .github/
│   └── workflows/                                      # CI/CD GitHub Actions
├── openspec/                                           # OpenSpec configuration
├── pom.xml                                             # Maven build definition
├── README.md                                           # Project documentation
└── CONTRIBUTING.md                                     # Contribution guide
```

## Core Modules

This is a single-module project (no Maven submodules). All source lives in `hu.blackbelt.judo.dispatcher.api`:

### Interfaces

| Type | Purpose |
|------|---------|
| `Dispatcher` | Routes API calls to operations via `callOperation()`; performs type coercion via `coerce()` |
| `Context` | Thread-safe key-value store for passing state through dispatch chains |
| `Sequence<T>` | Generates sequential values by name (`getNextValue`/`getCurrentValue`) |
| `VariableResolver` | Resolves typed environment variables by category and key |

### Classes

| Type | Purpose |
|------|---------|
| `JudoPrincipal` | Security principal (implements `java.security.Principal`) with name, realm, client, and attributes map |
| `BusinessException` | Domain exception (extends `RuntimeException`) with type, errorCode, details map, and locale |
| `FileType` | File metadata container with id, fileName, size, and mimeType (Lombok `@Builder`) |

### Annotation

| Type | Purpose |
|------|---------|
| `@JudoOperation` | Runtime-retained method annotation marking Dispatcher operations; carries the operation's fully qualified name |

## Technology Stack

### Core Technologies
- **Java 21** — compilation target
- **OSGi** (org.osgi.core 6.0.0, osgi.cmpn 6.0.0) — bundle packaging and service component annotations
- **Eclipse EMF** (org.eclipse.emf.ecore 2.12.0) — EMF model types referenced in Dispatcher interface
- **Google Guava** (30.0-jre) — runtime utility library
- **Gson** (2.9.1) — JSON serialization
- **SLF4J** (2.0.16) — logging facade
- **Lombok** (1.18.34) — `@Builder`, `@Getter`, `@AllArgsConstructor` code generation

### Build & Quality
- **Maven 3.9.4** with Maven Wrapper (`./mvnw`)
- **JUnit 5** (5.9.1) — unit testing
- **Mockito** (4.8.0) — mocking framework
- **Hamcrest** (2.2) — matcher assertions
- **JaCoCo** (0.8.12) — code coverage
- **SonarQube** (sonar-maven-plugin 3.9.1.2184) — static analysis
- **maven-bundle-plugin** (5.1.8) — OSGi bundle generation
- **flatten-maven-plugin** (1.3.0) — CI-friendly POM flattening

## Build Commands

```bash
# Run tests
./mvnw clean test

# Full build and install to local repository
./mvnw clean install

# Run a single test class
./mvnw test -Dtest=MyTestClass

# Run a single test method
./mvnw test -Dtest=MyTestClass#myMethod

# Code quality analysis
./mvnw sonar:sonar
```

### Maven Profiles

| Profile | Purpose |
|---------|---------|
| `sign-artifacts` | GPG-signs build artifacts using sign-maven-plugin |
| `release-dummy` | Deploys to local `/tmp/` directory for testing |
| `release-judong` | Deploys to JUDO-NG Nexus (https://nexus.judo.technology) |
| `release-central` | Deploys to Maven Central via Sonatype OSSRH |
| `generate-github-asciidoc-diagrams` | Renders AsciiDoc diagrams to PNG using AsciidoctorJ |
| `update-source-code-license` | Updates EPL 2.0 license headers in source files |

## Key Configuration Files

| File | Purpose |
|------|---------|
| `pom.xml` | Maven build definition with dependencies, plugins, and profiles |
| `.mvn/jvm.config` | JVM settings for Maven (heap 1024m-2048m, UTF-8 encoding) |
| `logback-test.xml` | Logback configuration for test execution |
| `.github/workflows/build.yml` | Main CI/CD pipeline (build, deploy, release) |
| `.github/workflows/release.yml` | Manual release trigger workflow |

## Development Environment

**Required:**
- Java 21 JDK
- Maven 3.9.4+ (or use the included `./mvnw` wrapper)

**Optional:**
- SonarQube server for code quality analysis
- Access to JUDO-NG Nexus for snapshot deployment

## Git Workflow

- **Main Branch:** `develop`
- **Release Branch:** `master` (latest released sources)
- **Versioning:** Semantic versioning, currently `1.0.3-SNAPSHOT`
- **Branch naming:** `feature/JNG-xxx_summary`, `bugfix/JNG-xxx_summary`, `support/JNG-xxx_summary`
- **CI/CD:** GitHub Actions (build, merge automation, release creation)

See [.github/CIFLOW.md](.github/CIFLOW.md) for the full branching and CI/CD documentation.

## Important Notes

1. **All commits must include a JIRA ticket number** (`JNG-xxx`) — this is enforced by convention
2. **This is a pure API library** — it defines contracts only, no implementations. Changes here affect all downstream JUDO-NG modules
3. **OSGi bundle** — exports `hu.blackbelt.judo.dispatcher.api*`; import version ranges are configured in `osgi-default-import` property
4. **Lombok** is used at compile time (provided scope) with delombok for Javadoc generation
5. **EPL 2.0 license headers** are required in all source files; use the `update-source-code-license` profile to update them
6. The `Dispatcher` exchange model uses well-known string keys (prefixed with `__`) defined as constants on the `Dispatcher` interface

## Related Documentation

- [README.md](README.md) — Project overview with API diagrams
- [CONTRIBUTING.md](CONTRIBUTING.md) — Contribution guide
- [.github/CIFLOW.md](.github/CIFLOW.md) — CI/CD and branching strategy
- [judo-community](https://github.com/BlackBeltTechnology/judo-community) — Parent ecosystem project
