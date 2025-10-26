# CLAUDE.md

mathematics: show your work. makes each step of solving the problem as streamlined, intuitive, engaging, and fun as possible 

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 3.5.6 application written in Kotlin 1.9.25, using Java 17. The project is called "skilltree" and is structured as a standard Spring Boot application with Gradle as the build tool.

## Build System

This project uses Gradle with the Kotlin DSL (`.gradle.kts` files).

### Common Commands

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.skilltree.skilltree.SkilltreeApplicationTests"

# Clean build artifacts
./gradlew clean

# Check for dependency updates
./gradlew dependencyUpdates
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

## Architecture

### Package Structure

The application follows standard Spring Boot conventions:
- Main package: `com.skilltree.skilltree`
- Main application class: `SkilltreeApplication.kt` - annotated with `@SpringBootApplication`
- Application entry point: `main()` function that calls `runApplication<SkilltreeApplication>()`

### Technology Stack

- **Language**: Kotlin 1.9.25 with JSR-305 strict mode enabled
- **Framework**: Spring Boot 3.5.6
- **Java Version**: 17 (configured via Java toolchain)
- **Build Tool**: Gradle with Kotlin DSL
- **Testing**: JUnit 5 (Jupiter) with Kotlin test support

### Key Dependencies

- `spring-boot-starter`: Core Spring Boot functionality
- `kotlin-reflect`: Kotlin reflection library (required for Spring)
- `spring-boot-starter-test`: Testing infrastructure
- `kotlin-test-junit5`: Kotlin testing extensions for JUnit 5

### Kotlin Configuration

The project uses strict JSR-305 nullability checks (`-Xjsr305=strict` compiler flag), which makes Spring Framework nullability annotations more strict in Kotlin.

## Development Notes

### Test Configuration

All tests use JUnit Platform (JUnit 5) configured in `build.gradle.kts` via `tasks.withType<Test> { useJUnitPlatform() }`.

### Application Properties

Configuration is managed through `src/main/resources/application.properties`. The application name is set to "skilltree".