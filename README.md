# 🚀 Restful Booker Test Automation Framework

This project is a Java-based, enterprise-grade test automation framework designed to run automated API tests against the [Restful Booker API](https://restful-booker.herokuapp.com/apidoc).

![Java Version](https://img.shields.io/badge/Java-21-orange.svg?style=flat-square&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.5-brightgreen.svg?style=flat-square&logo=springboot)
![Gradle](https://img.shields.io/badge/Gradle-8+-blue.svg?style=flat-square&logo=gradle)
![JUnit 5](https://img.shields.io/badge/JUnit-5.10.0-red.svg?style=flat-square&logo=junit5)
![RestAssured](https://img.shields.io/badge/RestAssured-5.5.6-blueviolet.svg?style=flat-square)
![Allure](https://img.shields.io/badge/Allure-2.26.0-green.svg?style=flat-square)
![Code Style](https://img.shields.io/badge/code%20style-Google_Java_Format-blue.svg?style=flat-square)

---

## 🏗️ Architecture & Component Design

The framework utilizes a highly modular, multi-layered architecture backed by **Spring Dependency Injection** to promote decoupling, clean boundaries, and reusable assertions.

### 📁 Directory Layout Overview
```text
src/
├── main/java/org/example/
│   ├── assertion/         # Domain-specific, fluent assertions (booking, auth, common)
│   ├── client/            # Low-level RestAssured clients configuring base URI and filters
│   ├── config/            # Spring @Configuration & application properties loading
│   ├── dataprovider/      # Parameterized payloads for data-driven JUnit tests
│   ├── factory/           # Entity generation workflows (BookingFactory, UserFactory)
│   ├── mapper/            # Converters & mapping layers (ResponseMapper, DateMapper)
│   ├── model/             # Lombok-powered request/response DTO models
│   ├── pool/              # Thread-safe entity caching / state management
│   ├── steps/             # Business-level workflow orchestrators & API interaction
│   ├── tags/              # Custom JUnit 5 annotations (@Regression, @Debug)
│   └── tracking/          # Central dictionary of known bugs and issues
└── main/resources/        # Environment profile-specific configuration files
```

---

## ⚡ Key Architectural Features

### 🔄 Thread-Safe Entity Pooling
To optimize execution speed and prevent rate-limiting or service degradation on the public API, the framework manages state using a thread-safe cache (`BookingDetailsPool`):
- Pushes newly-created entities to the pool.
- Retrieves or lazily initializes entities dynamically via `.popOrCreate()`.
- Guarantees test thread safety with non-blocking concurrent queues.

### 🐛 Smart Bug Tracking & Defect Isolation
We isolate test noise using explicit annotations coupled with a known bug registry:
- **`Bugs.java`**: Houses constants representing known target application bugs.
- **Defect Mapping**: Failing tests due to open bugs are decorated with Allure's `@Issue(...)` and JUnit's `@Disabled` to prevent false negatives in regression runs while keeping them linked.

---

## ⚙️ Configuration & Environment Profiles

Configurations are managed dynamically using Spring Boot properties.

**Available Profiles:** `dev`, `qa`
**Available Tags:** `regression`, `debug`, `bug`

### Active Environment Override
Define environment parameters inside `src/main/resources/application-{profile}.properties` and activate them via system properties. 

Here are examples showing how to execute tests with all possible configurations—setting the environment profile (`qa`) and filtering tests by combining include/exclude tags:

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
