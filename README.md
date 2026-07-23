# 🚀 Restful Booker Test Automation Framework

This project is a Java-based, enterprise-grade test automation framework designed to run automated API tests against the [Restful Booker API](https://restful-booker.herokuapp.com/apidoc). It uses a modern tech stack focused on maintainability, readability, and robust reporting.

![Java Version](https://img.shields.io/badge/Java-21-orange.svg?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.5-brightgreen.svg?style=flat-square&logo=springboot)
![Gradle](https://img.shields.io/badge/Gradle-8+-blue.svg?style=flat-square&logo=gradle)
![JUnit 5](https://img.shields.io/badge/JUnit-5.10.0-red.svg?style=flat-square&logo=junit5)
![RestAssured](https://img.shields.io/badge/RestAssured-5.5.6-blueviolet.svg?style=flat-square)
![Allure](https://img.shields.io/badge/Allure-2.26.0-green.svg?style=flat-square)
![Code Style](https://img.shields.io/badge/code%20style-Google_Java_Format-blue.svg?style=flat-square)

---

## 🛠 Tech Stack & Dependencies

The project is built with the following core technologies:

- **Language:** Java 21 (configured toolchain)
- **Dependency Management & Build:** Gradle
- **API Client:** RestAssured (version 5.5.6)
- **Test Runner:** JUnit 5 (JUnit BOM 5.10.0)
- **Dependency Injection:** Spring Boot Starter Test (version 3.5.5)
- **Reporting:** Allure (version 2.26.0) with AspectJ weaver (version 1.9.22.1) for test step instrumentation
- **Code Quality:** Spotless (version 6.22.0) using Google Java Format (version 1.30.0), Lombok (version 1.18.40)
- **Data Generation:** DataFaker (version 2.5.4)

---

## 🏗 Architecture & Design Layers

The framework utilizes a highly modular, multi-layered architecture backed by **Spring Dependency Injection** and **AssertJ Fluent Assertions** to promote decoupling, clean boundaries, and highly readable tests:

- **Dependency Injection in Tests:** Test classes are annotated with `@SpringBootTest` and directly autowire only the specific dependencies (clients, steps, pools, factories, assertions) they require. This prevents the "God Object" anti-pattern and couples tests only to the components they actually use.
- **API Layer (`org.example.api`):** Wraps RestAssured to make low-level HTTP calls. 
  - `RestApi` configures the base URI, content type, and Allure/logging filters.
  - `BookerApi` contains specific endpoint paths and request mapping definitions (e.g., ping, auth, booking).
- **Client Layer (`org.example.client`):** Contains business-level, developer-friendly orchestration clients. These clients make the low-level API calls, assert successful status codes, and map HTTP responses into clean domain objects/Strings for tests. Grouped into business sub-scopes (`booking`, `bookingdetails`, `token`, `health`).
- **Assertion Layer (`org.example.assertion`):** Provides domain-specific, fluent assertions (`common`, `booking`, `auth`).
- **Model Layer (`org.example.model`):** Holds DTOs (Data Transfer Objects) for request and response payloads, utilizing Lombok `@Data` and `@Builder` patterns.
- **Factory Layer (`org.example.factory`):** Encapsulates entity generation workflows (e.g., `BookingFactory`, `UserFactory`) for preparing test data.
- **Data Provider Layer (`org.example.dataprovider`):** Supplies parameterized payloads for JUnit parameterized tests (e.g., invalid fields, edge case inputs).
- **Pool Layer (`org.example.pool`):** Manages states of test entities across different test scopes to speed up execution.
- **Config Layer (`org.example.config`):** Uses Spring `@Configuration` to load environment properties.
- **Mapper Layer (`org.example.mapper`):** Contains components for object conversion and data translation (e.g., `ResponseMapper`, `DateMapper`).
- **Tracking Layer (`org.example.tracking`):** Houses definitions for known bugs and issues to keep test results clear.
- **Utilities (`org.example.utils`):** Shared, pure helper classes (e.g., collection utilities, custom string formatters).

---

## ⚡ Key Architectural Features

### 🔄 Thread-Safe Entity Pooling
To optimize execution speed and prevent rate-limiting or service degradation on the public API, the framework manages state using a thread-safe cache (`BookingDetailsPool`):
- Pushes newly-created entities to the pool.
- Retrieves or lazily initializes entities dynamically via `.popOrCreate()`.
- Guarantees test thread safety with non-blocking concurrent queues.

### 🧪 SDK-Grade Client & API Separation
- **API Layer**: Low-level endpoint pathways, headers, and HTTP request actions using RestAssured.
- **Client Layer**: High-level, developer-friendly orchestration APIs that run operations, verify response code boundaries, map JSON bodies to models, and return structured Java objects to tests.

### 🎯 Fluent Custom AssertJ Assertions
All assertions are implemented as specialized AssertJ classes inheriting from `AbstractAssert`. Tests achieve industry-standard readability without requiring Spring container autowiring:
```java
// Example of extremely readable assertions
BookingAssert.assertThat(response).isEqualToBooking(expectedBooking);
TokenAssert.assertThat(token).hasLengthOf(15);
```

### 🐛 Smart Bug Tracking & Defect Isolation
We isolate test noise using explicit annotations coupled with a known bug registry:
- **`Bugs.java`**: Houses constants representing known target application bugs.
- **Defect Mapping**: Failing tests due to open bugs are decorated with Allure's `@Issue(...)` and JUnit's `@Disabled` to prevent false negatives in regression runs while keeping them linked.

---

## ⚙️ Configuration & Environment

Configuration properties are loaded from `application.properties` into `SpringConfig.java`.

### Environment Profiles
The framework supports environment-specific profiles (e.g., `qa`, `dev`) loaded from profile-specific properties files (`application-dev.properties`, `application-qa.properties`).

To activate a profile:

| OS / Shell | Command (Environment + Tags) |
| :--- | :--- |
| **macOS / Linux** | `SPRING_PROFILES_ACTIVE=qa ./gradlew test -PincludeTags=regression -PexcludeTags=bug` |
| **PowerShell** | `$env:SPRING_PROFILES_ACTIVE="qa"; ./gradlew test -PincludeTags=regression -PexcludeTags=bug` |
| **Windows CMD** | `set SPRING_PROFILES_ACTIVE=qa && gradlew test -PincludeTags=regression -PexcludeTags=bug` |

### Environment Variables Overrides
To inject parameters at runtime (e.g., CI/CD), use the following overrides:
- `BOOKER_BASE_URL` - Target service base URL.
- `BOOKER_USERNAME` - Service authentication username.
- `BOOKER_PASSWORD` - Service authentication password.

---

## 🚀 Command Reference & Running Tests

### Prerequisites
- JDK 21
- Gradle (wrapper included)

### Key Commands

| Task | Command | Description |
| :--- | :--- | :--- |
| **Full Regression** | `./gradlew test` | Runs the complete JUnit 5 test suite |
| **Regression Suite Only** | `./gradlew test -PincludeTags=regression` | Runs only tests tagged with `@Regression` |
| **Debug Specific Tag** | `./gradlew test -PincludeTags=debug` | Runs tests matching the `@Debug` custom tag |
| **Skip Known Bugs** | `./gradlew test -PexcludeTags=bug` | Filters out tests currently flagged with unresolved bugs |
| **Apply Formatting** | `./gradlew spotlessApply` | Re-formats codebase using Google Java Format |
| **Check Formatting** | `./gradlew spotlessCheck` | Validates code styling compliance |
| **Clean Build** | `./gradlew clean build` | Standard gradle clean and compilation |

---

## 📊 Test Reporting (Allure)

### 1. Generating & Viewing Reports Locally
The project integrates Allure with AspectJ step instrumentation to generate beautiful reports with step logs.
```bash
./gradlew allureReport       # Generates HTML report in build/reports/allure-report
./gradlew allureServe        # Starts a local web server to instantly serve results
```

### 2. Reviewing GitHub Actions CI/CD Artifacts
When tests run in GitHub Actions:
1. Download and unzip the `reports` artifact.
2. Open your terminal **directly inside the unzipped `reports/` folder**.
3. Run the following to boot the web report:
   ```bash
   allure open allure-report/allureReport
   ```

---

## 📝 Development Conventions

- **Lombok Annotation Preferences:**
  - Use `@Data` for DTOs.
  - Use `@Builder` for constructing complex configurations or request payloads.
  - Use `@RequiredArgsConstructor` for constructor-based dependency injection in components.
- **Reporting Steps:**
  - Annotate client methods and step orchestrations with `@Step("Description")` to write detailed, human-readable step entries in the Allure reports.
- **Test Documentation:**
  - Every action or logical group of actions within a test method must be concisely described using inline comments.
- **Custom JUnit 5 Tags:**
  - Standard tags are defined under `org.example.tags` (e.g., `@Regression`, `@Debug`). Use these annotations instead of raw `@Tag("name")` strings.
- **Automated Formatting:**
  - The project strictly adheres to **Google Java Format**.
  - The `compileJava` task has a dependency on `spotlessApply`. Hence, compiling or running tests via `./gradlew` will automatically format files before execution.
- **Assertions Style:**
  - Prefer using custom assertion classes inside `org.example.assertion` extending AssertJ's `AbstractAssert<Self, Actual>` to enable native fluent testing interfaces.
  - Do not use static imports for `Assertions`. Always import `org.assertj.core.api.Assertions;` and use `Assertions.assertThat` explicitly in code (avoid fully-qualified class names like `org.assertj.core.api.Assertions.assertThat` in method bodies).
- **Data Providers vs Factories:**
  - Use **Data Providers** (`@MethodSource`) *only* for multi-parameter cases where the same test logic applies to various inputs.
  - Use **Factories** to provide objects for single-case scenarios to keep tests simpler and more direct.
