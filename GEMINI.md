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
- **API Layer (`org.example.api`):** 
  Wraps RestAssured to make low-level HTTP calls. 
  - [RestApi](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/api/RestApi.java) configures the base URI, content type, and Allure/logging filters.
  - [BookerApi](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/api/BookerApi.java) contains specific endpoint paths and request mapping definitions (e.g., ping, auth, booking).
- **Client Layer (`org.example.client`):** 
  Contains business-level, developer-friendly orchestration clients. These clients make the low-level API calls, assert successful status codes, and map HTTP responses into clean domain objects/Strings for tests. Grouped into business sub-scopes:
  - `booking` (e.g., `FetchBookingClient`, `DeleteBookingClient`, `UpdateBookingClient`, `PartialUpdateBookingClient`)
  - `bookingdetails` (e.g., `BookingDetailsClient` for booking creation)
  - `token` (e.g., `TokenClient` for authentication token operations)
  - `health` (e.g., `HealthClient` for ping checks)
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
- **Mapper Layer (`org.example.mapper`):** 
  Contains components for object conversion and data translation, such as [ResponseMapper](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/mapper/ResponseMapper.java) and [DateMapper](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/mapper/DateMapper.java).
- **Tracking Layer (`org.example.tracking`):** 
  Houses definitions for known bugs and issues to keep test results clear.
- **Utilities (`org.example.utils`):** 
  Shared, pure helper classes (e.g., collection utilities, custom string formatters).

---

## ⚙️ Configuration & Environment

Configuration properties are loaded from [application.properties](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/resources/application.properties) into [SpringConfig.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/config/SpringConfig.java).

### Environment Profiles
The framework supports environment-specific profiles (e.g., `qa`, `dev`) loaded from profile-specific properties files:
- [application-dev.properties](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/resources/application-dev.properties)
- [application-qa.properties](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/resources/application-qa.properties)

To activate a profile:
- **macOS/Linux:** `SPRING_PROFILES_ACTIVE=dev ./gradlew test`
- **Windows PowerShell:** `$env:SPRING_PROFILES_ACTIVE="dev"; ./gradlew test`
- **CI (GitHub Actions):** Handled via the workflow input environment variable mapping.

### Property Keys & Environment Overrides
The base configurations support overrides via standard environment variables:

```properties
booker.base-url=${BOOKER_BASE_URL:https://restful-booker.herokuapp.com}
booker.auth.username=${BOOKER_USERNAME:admin}
booker.auth.password=${BOOKER_PASSWORD:password123}
```

- `@ComponentScan` scans everything under the `org.example` package.
- `SpringConfig` instantiates a default `@Bean` user using the credentials mapped from the active profile or environment properties.

---

## 🔄 Entity Pooling Strategy

To optimize test execution speed and prevent the API from getting overwhelmed with redundant requests, we use a thread-safe entity pool:

* **Pool Component ([BookingDetailsPool](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/pool/BookingDetailsPool.java)):**
  - Manages a thread-safe `Queue<BookingDetails>` using `ConcurrentLinkedQueue` for non-blocking, thread-safe operations.
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
| **Generate Allure Report** | `./gradlew allureReport` |
| **Serve Allure Report** | `./gradlew allureServe` |

### Tag Configuration
* Tag filters are evaluated in [build.gradle](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/build.gradle):
  - `-PincludeTags`: Accepts comma-separated values to filter Junit tests.
  - `-PexcludeTags`: Accepts comma-separated values to skip tests.

### Viewing Reports
Allure report generation:

#### 1. Via Gradle Tasks (Recommended)
You can use the configured Allure Gradle plugin:
- Build the HTML report: `./gradlew allureReport` (outputs to `build/reports/allure-report`)
- Serve the report locally: `./gradlew allureServe`

#### 2. Via Allure CLI
- Generate and serve directly using Allure CLI:
  `allure serve build/allure-results`

#### 3. CI/CD Artifacts (GitHub Actions)
- The CI workflow runs tests and uploads the generated `build/reports` folder as an artifact named `reports`.
- Once downloaded and unzipped, open a terminal in the unzipped `reports/` folder and run:
  `allure open allure-report/allureReport`

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
  - **Always** use custom assertion classes inside `org.example.assertion` extending AssertJ's `AbstractAssert<Self, Actual>` to enable native fluent testing interfaces. Do not use Spring `@Component` classes with void-returning assertion methods.
  - Do not use static imports for `Assertions`. Always import `org.assertj.core.api.Assertions;` and use `Assertions.assertThat` explicitly in code (avoid fully-qualified class names like `org.assertj.core.api.Assertions.assertThat` in method bodies).
- **Data Providers vs Factories:**
  - Use **Data Providers** (`@MethodSource`) *only* for multi-parameter cases where the same test logic applies to various inputs.
  - Use **Factories** to provide objects for single-case scenarios to keep tests simpler and more direct.
