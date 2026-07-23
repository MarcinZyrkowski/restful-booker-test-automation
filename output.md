# 🔍 Code Quality Review — Restful Booker Test Automation

> **Date:** 2026-07-23  
> **Scope:** Full repository review — architecture, code quality, design patterns, test quality, CI/CD  
> **Verdict:** The project demonstrates **strong architectural foundations** with clean layered separation, good use of Spring DI, and thorough Allure reporting. Several medium-severity issues exist around design pattern consistency, dependency scoping, and CI configuration.

---

## 📊 Executive Summary

| Category | Score | Verdict |
|:---|:---:|:---|
| **Architecture & Layering** | ⭐⭐⭐⭐☆ | Well-structured with clear separation of concerns |
| **Code Quality** | ⭐⭐⭐☆☆ | Good conventions, but inconsistencies and dead code present |
| **Design Patterns** | ⭐⭐⭐☆☆ | Good patterns adopted, but not uniformly applied |
| **Test Quality** | ⭐⭐⭐⭐☆ | Thorough coverage, clean naming, minor pool management gaps |
| **Build & Dependencies** | ⭐⭐⭐☆☆ | Version catalog is clean, but dependency scopes are wrong |
| **CI/CD** | ⭐⭐☆☆☆ | Manual-only workflow, no automated triggers, security gaps |
| **Reporting (Allure)** | ⭐⭐⭐⭐☆ | Consistent Epic/Feature/Story hierarchy, missing `@Description` |

**Total Issues Found: 52**
- 🔴 Critical/High: **8**
- 🟡 Medium: **18**
- 🟢 Low: **26**

---

## Table of Contents

1. [Architecture & Design](#1-architecture--design)
2. [API Layer](#2-api-layer)
3. [Client Layer](#3-client-layer)
4. [Model Layer](#4-model-layer)
5. [Assertion Layer](#5-assertion-layer)
6. [Factory & Generator Layer](#6-factory--generator-layer)
7. [Mapper, Pool, Helper & Utils](#7-mapper-pool-helper--utils)
8. [Tags & Tracking](#8-tags--tracking)
9. [Test Classes](#9-test-classes)
10. [Build & Dependencies](#10-build--dependencies)
11. [CI/CD Pipeline](#11-cicd-pipeline)
12. [Configuration & Profiles](#12-configuration--profiles)
13. [Prioritized Action Items](#13-prioritized-action-items)

---

## 1. Architecture & Design

### ✅ Strengths

- **Clean layered architecture:** API → Client → Factory/Pool → Test — well-defined boundaries
- **Decorator pattern for auth** ([AuthRequestDecorator](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/api/AuthRequestDecorator.java)) is elegant and extensible
- **Entity pooling** ([BookingDetailsPool](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/pool/BookingDetailsPool.java)) with `ConcurrentLinkedQueue` is thread-safe
- **Constructor injection** used consistently across Spring components
- **Centralized version catalog** in [libs.versions.toml](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/gradle/libs.versions.toml)
- **Custom JUnit 5 tags** instead of raw `@Tag` strings
- **Step-builder pattern** in [BookingGenerator](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/generator/request/BookingGenerator.java) is well-designed

### ⚠️ Architectural Concerns

| # | Issue | Severity | Details |
|---|-------|----------|---------|

---

## 2. API Layer

### Files Reviewed

| File | Lines | Verdict |
|------|-------|---------|
| [RestApi.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/api/RestApi.java) | ~27 | ✅ Good |
| [BookerApi.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/api/BookerApi.java) | ~108 | ⚠️ Medium |
| [AuthRequestDecorator.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/api/AuthRequestDecorator.java) | ~35 | ✅ Good |

### Issues

| # | File | Issue | Severity |
|---|------|-------|----------|
| B1 | `RestApi` | Filter objects (`AllureRestAssured`, `RequestLoggingFilter`, `ResponseLoggingFilter`) are instantiated on every `basicRequest()` call. Should be cached as `static final` fields. | 🟡 Medium |

---

## 3. Client Layer

### Files Reviewed

| File | Verdict |
|------|---------|
| [DeleteBookingClient.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/client/booking/DeleteBookingClient.java) | ⚠️ Medium |
| [FetchBookingClient.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/client/booking/FetchBookingClient.java) | ✅ Good |
| [UpdateBookingClient.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/client/booking/UpdateBookingClient.java) | ⚠️ Medium |
| [PartialUpdateBookingClient.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/client/booking/PartialUpdateBookingClient.java) | ⚠️ Medium |
| [BookingDetailsClient.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/client/bookingdetails/BookingDetailsClient.java) | ⚠️ Medium |
| [TokenClient.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/client/token/TokenClient.java) | ⚠️ Medium |
| [HealthClient.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/client/health/HealthClient.java) | ✅ Good |

### Issues

| # | File | Issue | Severity |
|---|------|-------|----------|
| C2 | `BookingDetailsClient` | ~~`createBookingExpectingError` uses inline `Assertions.assertThat(statusCode).isIn(...)` instead of `ResponseAssertion`. This **breaks the established delegation pattern** used by every other client.~~ (Fixed: Refactored to use `ResponseAssert.assertThat()`) | 🟢 Fixed |
| C4 | `UpdateBookingClient` | Duplicate `@Step` descriptions — error methods share identical step text despite asserting different status codes. Allure cannot distinguish them. | 🟡 Deferred |
| C5 | `PartialUpdateBookingClient` | Same duplicate `@Step` issue as `UpdateBookingClient`. | 🟡 Deferred |

---

## 4. Model Layer

### Files Reviewed

| File | Verdict |
|------|---------|
| [Booking.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/model/service/dto/common/Booking.java) | ⚠️ Medium |
| [User.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/model/service/dto/request/auth/User.java) | ✅ Good |
| [Token.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/model/service/dto/response/auth/Token.java) | ✅ Good |
| [ErrorResponse.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/model/service/dto/response/auth/ErrorResponse.java) | 🟢 Low |
| [BookingDetails.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/model/service/dto/response/booking/BookingDetails.java) | ✅ Good |
| [BookingId.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/model/service/dto/response/booking/BookingId.java) | ✅ Good |
| [AdditionalNeed.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/model/helper/AdditionalNeed.java) | 🟢 Low |
| [StringResponseBody.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/model/service/enums/StringResponseBody.java) | 🟢 Low |

### Issues

| # | File | Issue | Severity |
|---|------|-------|----------|

---

## 5. Assertion Layer

### Files Reviewed

| File | Pattern | Verdict |
|------|---------|---------|
| [TokenAssert.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/assertion/auth/TokenAssert.java) | `AbstractAssert` ✅ | ⚠️ Medium |
| [BookingAssert.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/assertion/booking/BookingAssert.java) | `AbstractAssert` ✅ | 🔴 High |
| [BookingDetailsAssert.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/assertion/booking/BookingDetailsAssert.java) | `AbstractAssert` ✅ | ✅ Good |
| [BookingIdListAssert.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/assertion/booking/BookingIdListAssert.java) | `AbstractAssert` ✅ | ⚠️ Medium |
| [ErrorResponseAssert.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/assertion/common/ErrorResponseAssert.java) | `AbstractAssert` ✅ | 🟢 Low |
| [ResponseAssertion.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/assertion/common/ResponseAssertion.java) | `@Component` ❌ | 🟡 Medium |
| [StringResponseAssert.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/assertion/common/StringResponseAssert.java) | `AbstractAssert` ✅ | ⚠️ Medium |
| [AssertionUtils.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/assertion/common/AssertionUtils.java) | `@Component` ❌ | 🔴 Dead Code |

### Issues

| # | File | Issue | Severity |
|---|------|-------|----------|
| E7 | `BookingIdListAssert` | Missing `doesNotContainBookingId(int)` method — common need for negative assertions. | 🟢 Low |

---

## 6. Factory & Generator Layer

### Files Reviewed

| File | Verdict |
|------|---------|
| [BookingFactory.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/factory/booking/BookingFactory.java) | ✅ Good |
| [UserFactory.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/factory/auth/UserFactory.java) | ✅ Good |
| [BookingGenerator.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/generator/request/BookingGenerator.java) | ⚠️ Medium |
| [UserGenerator.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/generator/request/UserGenerator.java) | 🟢 Low |
| [DateTimesGenerator.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/generator/DateTimesGenerator.java) | ⚠️ Medium |
| [BookingDataProvider.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/dataprovider/BookingDataProvider.java) | ✅ Good |

### Issues

| # | File | Issue | Severity |
|---|------|-------|----------|
| F1 | `BookingGenerator` | `withRandomMissingRequiredFields()` uses a non-deterministic `while(!anyFieldModified)` loop — all 5 `randomBoolean()` calls could return `false`. Should deterministically pick at least one field. | 🟡 Medium |
| F2 | `BookingGenerator` | `totalPrice` uses `randomLong` + `(int)` cast. Should use `randomInt` directly. | 🟢 Low |
| F3 | `BookingGenerator` | `withMissingCheckin()`/`withMissingCheckout()` will NPE if called after `withMissingBookingDates()`. No precondition check. | 🟢 Low |
| F6 | `UserGenerator` | Over-engineered — single preset method (`withInvalidCredentials()`) doesn't justify the full builder ceremony. A static factory method would suffice. | 🟢 Low |

---

## 7. Mapper, Pool, Helper & Utils

### Issues

| # | File | Issue | Severity |
|---|------|-------|----------|
| G1 | [DateMapper](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/mapper/DateMapper.java) | Trivially thin wrapper around `LocalDate.parse()`. If the model used `LocalDate` directly, this class would be unnecessary. | 🟡 Medium |
| G2 | [ResponseMapper](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/mapper/ResponseMapper.java) | No error handling for deserialization failures. Failed mapping produces cryptic RestAssured exceptions. Add try-catch with descriptive context. | 🟡 Medium |
| G4 | [BookingDetailsPool](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/pool/BookingDetailsPool.java) | `push(Response)` overload couples the pool to the HTTP layer. Pool should only accept domain objects — callers should map before pushing. | 🟡 Medium |
| G5 | [BookingDetailsPool](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/pool/BookingDetailsPool.java) | No null validation on `push()`. `ConcurrentLinkedQueue.offer(null)` will throw NPE. Add `Objects.requireNonNull()`. | 🟢 Low |
| G7 | [BookerRandomUtils](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/utils/BookerRandomUtils.java) | Uses `RandomUtils.secureStrong()` — cryptographically strong RNG is overkill for test data. Use `RandomUtils.insecure()` for better performance. | 🟡 Medium |
| G8 | [BookerStringUtils](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/utils/BookerStringUtils.java) | `randomFullName()` and `randomSentence()` are pointless wrappers that just delegate to `FakerUtils`. Remove them and use `FakerUtils` directly. | 🟡 Medium |

---

## 8. Tags & Tracking

### Files Reviewed

| File | Verdict |
|------|---------|
| [Regression.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/tags/Regression.java) | ✅ Good |
| [Debug.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/tags/Debug.java) | ✅ Good |
| [Bugs.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/tracking/Bugs.java) | ⚠️ Medium |

### Issues

| # | File | Issue | Severity |
|---|------|-------|----------|
| H2 | `Bugs` | Declared as `interface` — older Java idiom. Use `public final class` with private constructor instead. | 🟢 Low |
| H3 | `Bugs` | Bug constants contain paragraph-length descriptions. Consider short IDs as constants and descriptions as Javadoc comments. | 🟢 Low |

---

## 9. Test Classes

### Files Reviewed

| File | Tests | Verdict |
|------|-------|---------|
| [AuthTest.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/test/java/org/example/auth/AuthTest.java) | 2 | ✅ Good |
| [HealthCheckTest.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/test/java/org/example/health/HealthCheckTest.java) | 1 | ✅ Good |
| [CreateBookingTest.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/test/java/org/example/booking/create/CreateBookingTest.java) | 4 | ⚠️ Medium |
| [DeleteBookingTest.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/test/java/org/example/booking/delete/DeleteBookingTest.java) | 3 | ⚠️ Medium |
| [FetchBookingTest.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/test/java/org/example/booking/fetch/FetchBookingTest.java) | 3 | ✅ Good |
| [FetchBookingsIdsTest.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/test/java/org/example/booking/fetch/FetchBookingsIdsTest.java) | 10 | ⚠️ Medium |
| [UpdateBookingTest.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/test/java/org/example/booking/update/UpdateBookingTest.java) | 4 | ✅ Good |
| [PartialUpdateBookingTest.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/test/java/org/example/booking/update/PartialUpdateBookingTest.java) | 5 | ⚠️ Medium |

### ✅ Test Strengths

- Consistent `@SpringBootTest(classes = SpringConfig.class)` across all tests
- Clean AAA pattern with blank-line separation
- Thorough Allure annotations (`@Epic`, `@Feature`, `@Story`, `@Severity`)
- Excellent double-verification in update/delete tests (assert response + fetch to confirm)
- Good use of entity pool for test data lifecycle
- Parallel execution enabled via `junit-platform.properties`
- Well-structured Allure hierarchy:

```
├── Booking API
│   └── Booking Management
│       ├── Create Booking (CRITICAL)
│       ├── Delete Booking (CRITICAL)
│       ├── Fetch Booking (NORMAL)
│       ├── Fetch Booking IDs (NORMAL)
│       ├── Update Booking (CRITICAL)
│       └── Partial Update Booking (NORMAL)
├── Security & Identity
│   └── Authentication (BLOCKER)
└── System Infrastructure
    └── Health Check (BLOCKER)
```

### Issues

| # | File | Issue | Severity |
|---|------|-------|----------|
| I1 | All tests | **Missing `@Description`** — zero `@Description` annotations across all 8 test classes. Missed opportunity for Allure report enrichment. | 🟡 Medium |
| I2 | All tests | **No base test class** — `@SpringBootTest(classes = SpringConfig.class)` repeated 8 times. Extract to a composed annotation (e.g., `@IntegrationTest`). | 🟡 Medium |
| I3 | `DeleteBookingTest` | `deleteBookingUsingInvalidTokenTest` pops from pool but **does not push back** — booking still exists on server but is lost from pool. | 🟡 Medium |
| I4 | `PartialUpdateBookingTest` | `partialUpdateBookingUsingBasicAuthTest` and `partialUpdateBookingUsingTokenTest` **do not push back** the modified booking to the pool. Inconsistent with `UpdateBookingTest` which pushes back. | 🟡 Medium |
| I5 | `FetchBookingsIdsTest` | `fetchBookingIdsWithMixedFiltersTest` uses `randomOf(null, value)` — introduces **non-deterministic behavior** that harms test reproducibility. | 🟡 Medium |
| I6 | `CreateBookingTest` | **`@TestInstance(PER_CLASS)`** is only used on this one class (for non-static `@MethodSource`). All other 7 classes use default `PER_METHOD`. This inconsistency should be documented. | 🟢 Low |
| I8 | `FetchBookingsIdsTest` | `@Disabled` + `@Issue` tests are missing `@Bug` tag — means `-PexcludeTags=bug` has no effect on them. | 🟡 Medium |

---

## 10. Build & Dependencies

### Issues

| # | File | Issue | Severity |
|---|------|-------|----------|
| J1 | [build.gradle](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/build.gradle) | **`spring-boot-starter-test`, `rest-assured`, `allure-*`, `datafaker`, `jackson-databind` are declared `implementation` instead of `testImplementation`.** These are test dependencies and should not be on the production classpath. | 🔴 High |
| J2 | `build.gradle` | **Duplicate `useJUnitPlatform()`** — configured in both `tasks.withType(Test).configureEach` (line 54) and `test` block (line 81). Merge into single block. | 🟡 Medium |
| J3 | `build.gradle` | `agent` configuration has `canBeConsumed = true` — should be `false` since it's only used internally to resolve AspectJ weaver. | 🟢 Low |
| J4 | `build.gradle` | No `testLogging` block — test failures in CI show minimal output, making debugging harder. Add `events "passed", "skipped", "failed"` and `exceptionFormat "full"`. | 🟡 Medium |
| J5 | `build.gradle` | No `maxParallelForks` configuration — tests default to single-threaded Gradle execution. | 🟢 Low |
| J6 | `libs.versions.toml` | Unused entries: `spring-boot-gradle-plugin` library, `java` version (hardcoded in build.gradle), `lombok` library (managed by Freefair plugin). Clean up dead config. | 🟢 Low |
| J7 | `libs.versions.toml` | No Dependabot/Renovate configuration — dependencies will go stale without automated update PRs. | 🟡 Medium |

---

## 11. CI/CD Pipeline

### File Reviewed

- [manual-test-run.yml](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/.github/workflows/manual-test-run.yml)

### Issues

| # | Issue | Severity |
|---|-------|----------|
| K1 | **No automated CI trigger** — only `workflow_dispatch` (manual). No `push`/`pull_request` workflow exists. Code can be merged untested. | 🔴 High |
| K2 | `contents: write` permission is broader than needed. Only `contents: read` is required for checkout + test + upload. | 🟡 Medium |
| K3 | GitHub Actions not pinned to commit SHAs — only major version tags (e.g., `@v4`). Supply-chain security risk. | 🟡 Medium |
| K4 | No `timeout-minutes` on the job — if tests hang, runner consumes minutes up to GitHub's 6-hour default. | 🟡 Medium |
| K5 | No `concurrency` block — multiple manual dispatches can run in parallel, wasting runner minutes. | 🟢 Low |
| K6 | No Gradle wrapper validation step — a compromised `gradle-wrapper.jar` could execute arbitrary code. | 🟡 Medium |
| K7 | No JUnit test reporter step (e.g., `dorny/test-reporter`) — test results require artifact download to view. | 🟢 Low |
| K8 | Separate `./gradlew clean` step is redundant on fresh CI runner — merge with `./gradlew test`. | 🟢 Low |
| K9 | Missing `distributionSha256Sum` in [gradle-wrapper.properties](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/gradle/wrapper/gradle-wrapper.properties) — no integrity verification for Gradle distribution. | 🟡 Medium |

---

## 12. Configuration & Profiles

### Files Reviewed

- [SpringConfig.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/config/SpringConfig.java)
- [AllureEnvironmentWriter.java](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/java/org/example/config/AllureEnvironmentWriter.java)
- [application.properties](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/resources/application.properties)
- [application-dev.properties](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/resources/application-dev.properties)
- [application-qa.properties](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/src/main/resources/application-qa.properties)

### Issues

| # | File | Issue | Severity |
|---|------|-------|----------|
| L1 | `SpringConfig` | Profile validation hardcodes allowed profiles `("qa", "dev")`. Extract to a constant `Set.of("qa", "dev")` — adding a profile requires code change. | 🟡 Medium |
| L2 | `SpringConfig` | `@Getter` on the config class exposes `env` field publicly via `getEnv()`, leaking the Spring `Environment` object. Use `@Getter` only on needed fields. | 🟢 Low |
| L3 | `application-dev/qa.properties` | **Profile files shadow env var overrides.** Base `application.properties` uses `${BOOKER_BASE_URL:...}` but profile files hardcode values without `${}` syntax. Setting `BOOKER_BASE_URL` env var while running with `SPRING_PROFILES_ACTIVE=dev` **will not work** because the profile file overrides it. | 🔴 High |
| L4 | `AllureEnvironmentWriter` | **Silent exception swallowing** — `catch (IOException e) { // Silently ignore }`. At minimum, log a warning. | 🟡 Medium |
| L5 | `AllureEnvironmentWriter` | Hardcoded path `"build/allure-results"`. Should be configurable or derived from Allure properties. | 🟢 Low |
| L6 | `AllureEnvironmentWriter` | `mkdirs()` return value unchecked. | 🟢 Low |
| L7 | `.gitignore` | Missing entries for `.env`, `allure-results/`, `allure-report/`, `.continue/`, `.gemini/`, `*.log`. | 🟢 Low |

---

## 13. Prioritized Action Items

### 🔴 P0 — Critical (Fix ASAP)

| # | Action | Impact |
|---|--------|--------|
| 2 | **Fix profile-specific properties** — either use `${ENV_VAR:default}` syntax in dev/qa files or remove duplicate properties | Environment variable overrides are silently broken |
| 5 | **Add automated CI trigger** — add `push`/`pull_request` workflow | Code can currently be merged without any test gate |

### 🟡 P1 — Medium (Next Sprint)

| # | Action | Impact |
|---|--------|--------|
| 6 | Change dependency scopes to `testImplementation` in `build.gradle` | Correct dependency hygiene |
| 7 | Add `@Description` annotations to all test methods | Improve Allure report quality |
| 8 | Extract `@SpringBootTest` boilerplate to composed annotation or base class | Reduce test boilerplate |
| 9 | Fix pool push-back in `PartialUpdateBookingTest` and `DeleteBookingTest` | Prevent pool depletion under parallel execution |
| 11 | Switch `secureStrong()` to `insecure()` in `BookerRandomUtils`/`BookerStringUtils` | Performance improvement |
| 12 | Add Gradle caching + wrapper validation to CI workflow | Faster, more secure CI |
| 13 | Add `testLogging` block to `build.gradle` | Better CI debugging |
| 15 | Unify `@Step` descriptions in `UpdateBookingClient`/`PartialUpdateBookingClient` | Allure report clarity |
| 17 | Make `fetchBookingIdsWithMixedFiltersTest` deterministic | Test reproducibility |
| 18 | Add Dependabot configuration for automated dependency updates | Security maintenance |

### 🟢 P2 — Low (Backlog)

| # | Action | Impact |
|---|--------|--------|
| 19 | Normalize `bookingId` type (`int` vs `String`) across `BookerApi` | API layer consistency |
| 21 | Remove `BookerStringUtils` delegation wrappers | Reduce unnecessary indirection |
| 22 | Consider migrating dates from `String` to `LocalDate` in model | Type safety (large refactor) |
| 23 | Cache filter instances in `RestApi.basicRequest()` | Minor performance |
| 25 | Add `distributionSha256Sum` to `gradle-wrapper.properties` | Supply-chain security |
| 26 | Pin GitHub Actions to commit SHAs | Supply-chain security |
| 27 | Add `.env`, `.DS_Store`, `allure-results/` to `.gitignore` | Repository hygiene |
| 29 | Refactor `withRandomMissingRequiredFields()` to be deterministic | Test reliability |

---

> **Review conducted using automated multi-agent analysis covering: API layer, client layer, model/factory/generator, assertion/tags/tracking, test classes, and CI/CD infrastructure.**
