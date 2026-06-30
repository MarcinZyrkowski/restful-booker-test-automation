# Restful Booker Test Automation

This project is a Java-based test automation framework for the [Restful Booker API](https://restful-booker.herokuapp.com/apidoc). It uses a modern tech stack focused on maintainability, readability, and robust reporting.

---

## 🛠 Tech Stack & Dependencies

The project is built with the following core technologies (specified in [build.gradle](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/build.gradle)):

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

The project follows a multi-layered architecture to separate concerns, enforce clean boundaries, and improve code reusability:

- **Dependency Injection in Tests:** 
  Test classes are annotated with `@SpringBootTest` and directly autowire only the specific dependencies (clients, steps, pools, factories, assertions) they require. This prevents the "God Object" anti-pattern and couples tests only to the components they actually use.
- **Client Layer (`org.example.client`):** 
  Wraps RestAssured to make low-level HTTP calls. 
  - [RestClient](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/client/RestClient.java) configures the base URI, content type, and Allure/logging filters.
  - [BookerClient](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/client/BookerClient.java) contains specific endpoint paths and request mapping definitions (e.g., ping, auth, booking).
- **Steps Layer (`org.example.steps`):** 
  Contains business-level workflow orchestrations. 
  - [BookerClientSteps](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/steps/BookerClientSteps.java) orchestrates client requests, performs status assertions, and maps responses into DTOs.
- **Assertion Layer (`org.example.assertion`):** 
  Provides domain-specific, fluent assertions. Divided into package sub-scopes:
  - `common` (e.g., status codes, error messages)
  - `booking` (e.g., booking details validation, ID presence checks)
  - `auth` (e.g., token response structure validations)
- **Model Layer (`org.example.model`):** 
  Holds DTOs (Data Transfer Objects) for request and response payloads, utilizing Lombok `@Data` and `@Builder` patterns.
- **Factory Layer (`org.example.factory`):** 
  Encapsulates entity generation workflows (e.g., `BookingFactory`, `UserFactory`) for preparing test data.
- **Data Provider Layer (`org.example.dataprovider`):** 
  Supplies parameterized payloads for JUnit parameterized tests (e.g., invalid fields, edge case inputs).
- **Pool Layer (`org.example.pool`):** 
  Manages states of test entities across different test scopes to speed up execution.
- **Config Layer (`org.example.config`):** 
  Uses Spring `@Configuration` to load environment properties.
- **Tracking Layer (`org.example.tracking`):** 
  Houses definitions for known bugs and issues to keep test results clear.
- **Utilities (`org.example.utils`):** 
  Shared, pure helper classes (e.g., collection utilities, custom string formatters).

---

## ⚙️ Configuration & Environment

Environment variables and configurations are mapped from [application.properties](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/resources/application.properties) into [SpringConfig.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/config/SpringConfig.java):

```properties
base_url=https://restful-booker.herokuapp.com
app.username=admin
app.password=password123
```

- `@ComponentScan` scans everything under the `org.example` package.
- `SpringConfig` instantiates a default `@Bean` user using the credentials mapped from `application.properties`.

---

## 🔄 Entity Pooling Strategy

To optimize test execution speed and prevent the API from getting overwhelmed with redundant requests, we use a thread-safe entity pool:

* **Pool Component ([BookingDetailsPool](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/pool/BookingDetailsPool.java)):**
  - Manages a thread-safe `Queue<BookingDetails>`.
  - Use `bookingDetailsPool.popOrCreate()` to pop an existing booking or create one dynamically if the pool is empty.
  - After creating a booking in a test, push it to the pool: `bookingDetailsPool.push(response)`.
  - Bookings are treated as immutable records to prevent state mutation race conditions.

---

## 🐛 Bug & Issue Tracking Conventions

Known issues in the target API are explicitly tracked using constants and test annotations:

* **Known Bug Dictionary ([Bugs](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/tracking/Bugs.java)):**
  Lists descriptions of target API bugs (e.g., `NEGATIVE_TOTAL_PRICE_BUG`, `CHECK_IN_BUG`, `CHECK_OUT_BUG`).
* **Test Annotations:**
  When a test uncovers a bug that is currently unresolved:
  1. Add `@Issue(value = Bugs.BUG_CONSTANT)` to link it in Allure.
  2. Combine it with `@Disabled("Skipped because of bug: " + Bugs.BUG_CONSTANT)` to prevent failure noise in regression runs.

---

## 🚀 Building and Running

### Prerequisites
- JDK 21
- Gradle (wrapper included)

### Key Commands

| Action | Command |
| :--- | :--- |
| **Run All Tests** | `./gradlew test` |
| **Run Regression Suite** | `./gradlew test -PincludeTags=regression` |
| **Run Debug Suite** | `./gradlew test -PincludeTags=debug` |
| **Run Excluding Tags** | `./gradlew test -PexcludeTags=bug` |
| **Apply Formatting** | `./gradlew spotlessApply` |
| **Check Formatting** | `./gradlew spotlessCheck` |
| **Clean Build** | `./gradlew clean build` |

### Tag Configuration
* Tag filters are evaluated in [build.gradle](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/build.gradle):
  - `-PincludeTags`: Accepts comma-separated values to filter Junit tests.
  - `-PexcludeTags`: Accepts comma-separated values to skip tests.

### Viewing Reports
Allure report generation:
1. Ensure the Allure CLI is installed on your local machine.
2. The JUnit run outputs are stored under `build/allure-results`.
3. Command to view the interactive server: `allure serve build/allure-results`.

---

## 📝 Development Conventions

- **Lombok Annotation Preferences:**
  - Use `@Data` for DTOs.
  - Use `@Builder` for constructing complex configurations or request payloads.
  - Use `@RequiredArgsConstructor` for constructor-based dependency injection in components.
- **Reporting Steps:**
  - Annotate client methods and step orchestrations with `@Step("Description")` to write detailed, human-readable step entries in the Allure reports.
- **Custom JUnit 5 Tags:**
  - Standard tags are defined under `org.example.tags` (e.g., `@Regression`, `@Debug`). Use these annotations instead of raw `@Tag("name")` strings.
- **Automated Formatting:**
  - The project strictly adheres to **Google Java Format**.
  - The `compileJava` task has a dependency on `spotlessApply`. Hence, compiling or running tests via `./gradlew` will automatically format files before execution.
- **Assertions Style:**
  - Prefer using custom assertion classes inside `org.example.assertion` to maintain readability and enable expressive testing workflows.
- **Data Providers vs Factories:**
  - Use **Data Providers** (`@MethodSource`) *only* for multi-parameter cases where the same test logic applies to various inputs.
  - Use **Factories** to provide objects for single-case scenarios to keep tests simpler and more direct.
