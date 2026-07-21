# 📋 Comprehensive Code Review & Architectural Strategy Report

This report outlines key observations, architectural feedback, and actionable design recommendations for the **Restful Booker Test Automation Framework**. 

Overall, this framework displays high maturity: it leverages modern Java 21 features (specifically **Java Records**), handles environment configurations cleanly via **Spring Boot**, and enforces great coding styles using **Spotless (Google Java Format)**. 

However, several deep-seated architectural inconsistencies, state-management leaks, and procedural design choices can be improved to elevate this repository to a true enterprise-grade, highly resilient automation product.

---

## 🏛️ 1. Architecture & Design Principles

### 🔄 Separation of Concerns in Response Mapping (Single Responsibility Principle)

#### 🛑 Current State
Currently, several customized assertion classes (e.g., `BookingDetailsAssertion`, `BookingAssertion`, `BookingIdAssertion`) and the `BookingDetailsPool` have direct dependencies on `ResponseMapper` and RestAssured's `Response` objects. They perform response deserialization inside assertion steps:

```java
// BookingDetailsAssertion.java
@Step("Assert that booking details are created from the given booking request")
public void assertResponseIsCreatedFrom(Response response, Booking booking) {
  responseAssertion.assertStatusCodeIsOk(response);
  BookingDetails bookingDetails = responseMapper.mapToCreateBookingResponse(response); // Deserialization inside assertion!
  assertBookingDetailsCreatedFrom(bookingDetails, booking);
}
```

#### ⚠️ Issue
* **Violation of SRP:** Assertion classes should only be responsible for validation and comparisons, not for parsing network responses or understanding JSON schemas.
* **Testing Tight Coupling:** If the mapping logic, JSON deserializer, or response container changes, both the mappers and assertions must be updated. It also prevents reusing assertions against objects obtained from non-HTTP sources (such as message queues or databases).

#### 💡 Recommendation
Enforce a clean architectural boundary where assertions accept strongly-typed DTOs instead of raw `Response` objects:
$$\text{Response} \longrightarrow \text{Mapper/Steps} \longrightarrow \text{DTO Model} \longrightarrow \text{Fluent Assertion}$$

Modify your step layers or tests to perform the deserialization, and pass the resulting Java Record directly into the assertion:

```java
// BookingDetailsAssertion.java
@Step("Assert that booking details match the expected booking request")
public void assertBookingDetailsMatch(BookingDetails actualDetails, Booking expectedBooking) {
  Assertions.assertThat(actualDetails).isNotNull();
  Assertions.assertThat(actualDetails.booking()).isNotNull();
  Assertions.assertThat(actualDetails.bookingId()).isNotNull();
  assertionUtils.assertEquals(actualDetails.booking(), expectedBooking);
}
```

---

### 🔑 Decoupled Client Authentication & Request Configuration

#### 🛑 Current State
In `BookerClient.java`, request authentication is scattered and handled using procedural method overloading:

```java
public Response updateBooking(int bookingId, Booking booking) {
  return basicRequest().auth().preemptive().basic(username, password)...
}

public Response updateBooking(int bookingId, Booking booking, String token) {
  return basicRequest().header(tokenCookieHeader(token))...
}
```

#### ⚠️ Issue
* **Duplication:** Auth configurations (preemptive basic authentication, custom Cookie headers) are duplicated and hardcoded in multiple HTTP execution paths.
* **Scalability:** If the authentication scheme changes (e.g., switching to JWT Bearer headers instead of Cookie-based auth), multiple client methods must be rewritten.

#### 💡 Recommendation
Implement an **Authentication Filter** or a **Request Specification Decorator Pattern**. For example, introduce an `AuthRequestSpecification` or leverage RestAssured's native `Filter` to dynamically apply security:

```java
@Component
@RequiredArgsConstructor
public class AuthRequestSpecification {

  private final SpringConfig springConfig;

  public RequestSpecification applyBasicAuth(RequestSpecification spec) {
    return spec.auth().preemptive().basic(springConfig.getUsername(), springConfig.getPassword());
  }

  public RequestSpecification applyTokenAuth(RequestSpecification spec, String token) {
    return spec.cookie("token", token);
  }
}
```
This abstracts security concerns completely out of individual path-level client methods, keeping endpoint declarations clean and simple.

---

## 💾 2. State & Lifecycle Management

### 🚰 The "BookingDetailsPool" Leakage Vulnerability

#### 🛑 Current State
The thread-safe queue in `BookingDetailsPool` relies on a manual lease-and-return approach across tests:

```java
BookingDetails bookingDetails = bookingDetailsPool.popOrCreate();
// ... execute test commands ...
bookingDetailsPool.push(bookingDetails); // Return resource
```

#### ⚠️ Issue
* **Resource Leakage under Test Failures:** If any assertion or client operation fails during the test execution, the rest of the `@Test` method is terminated. As a result, `bookingDetailsPool.push(bookingDetails)` is never reached, leaking the booking entirely and draining the pool.
* **Pool Depletion on State Mutation:** Some tests (like `PartialUpdateBookingTest`) pop bookings from the pool and perform state updates on the server, but omit returning them. This prevents stale state reuse but continuously starves the pool, forcing expensive recreation.

#### 💡 Recommendation
To solve both problems gracefully, implement the **Lent/Loan Resource Pattern** using Java's `AutoCloseable` interface. This guarantees safe check-ins even during catastrophic assertion failures:

```java
public class LentBooking implements AutoCloseable {
  private final BookingDetails bookingDetails;
  private final BookingDetailsPool pool;
  private boolean isDeleted = false;

  public LentBooking(BookingDetails bookingDetails, BookingDetailsPool pool) {
    this.bookingDetails = bookingDetails;
    this.pool = pool;
  }

  public BookingDetails get() {
    return bookingDetails;
  }

  public void markDeleted() {
    this.isDeleted = true;
  }

  @Override
  public void close() {
    if (!isDeleted) {
      pool.push(bookingDetails); // Automatically returned to the pool!
    }
  }
}
```

tests can then employ a clean **try-with-resources** block to auto-manage state lifecycle:

```java
@Test
void fetchBookingTest() {
  try (LentBooking lent = bookingDetailsPool.checkout()) {
    BookingDetails bookingDetails = lent.get();
    Response fetchResponse = bookerClient.getBookingById(String.valueOf(bookingDetails.bookingId()));
    bookingAssertion.assertResponseIsEqualTo(fetchResponse, bookingDetails.booking());
  } // Automatically returns the booking back to the pool, even if assertion fails!
}
```

---

## 🧹 3. Clean Code & Expressiveness

### 📈 Converting Procedural Assertions to Fluent Assertions

#### 🛑 Current State
Custom assertion classes currently contain procedural `void` methods, wrapping AssertJ underneath.

```java
bookingIdAssertion.assertBookingIdsAreNotEmpty(response);
```

#### ⚠️ Issue
This approach results in repetitive, verbose assertions when checking multiple criteria. It lacks the cohesive readability of standard fluent APIs.

#### 💡 Recommendation
Create custom AssertJ-style assertion classes by extending `AbstractAssert`. This allows elegant chaining directly in tests:

```java
public class BookingAssert extends AbstractAssert<BookingAssert, Booking> {

  public BookingAssert(Booking actual) {
    super(actual, BookingAssert.class);
  }

  public static BookingAssert assertThat(Booking actual) {
    return new BookingAssert(actual);
  }

  public BookingAssert hasFirstName(String firstName) {
    isNotNull();
    if (!actual.firstName().equals(firstName)) {
      failWithMessage("Expected booking's first name to be <%s> but was <%s>", firstName, actual.firstName());
    }
    return this;
  }

  public BookingAssert isDepositPaid() {
    isNotNull();
    if (!Boolean.TRUE.equals(actual.depositPaid())) {
      failWithMessage("Expected deposit to be paid");
    }
    return this;
  }
}
```

This transforms your tests to read with premium fluency:

```java
BookingAssert.assertThat(actualBooking)
    .hasFirstName("Sally")
    .isDepositPaid();
```

---

### 🏗️ Stateless Generators vs. Stateful Payload Builders

#### 🛑 Current State
`BookingGenerator` and `UserGenerator` maintain local state inside builder instances:

```java
public class BookingGenerator {
  private Booking booking; // Stateful instance variable!

  public static BookingGenerator builder() {
    return new BookingGenerator();
  }
  
  public BookingGenerator withAllValidFields() {
    this.booking = Booking.builder()...build();
    return this;
  }
}
```

#### ⚠️ Issue
Having instance-state variables in helper generators increases the risk of thread-safety violations if a generator instance is accidentally shared, autowired, or static-referenced across concurrent test executions.

#### 💡 Recommendation
Keep generators fully stateless. Use Lombok’s native `@Builder(toBuilder = true)` on the `Booking` record to customize and clone payloads immutably:

```java
public class BookingGenerator {

  public static Booking createValidBooking() {
    return Booking.builder()
        .firstName(FakerUtils.generateFirstName())
        .lastName(FakerUtils.generateLastName())
        .totalPrice((int) BookerRandomUtils.randomNumber(50, 100))
        .depositPaid(true)
        .bookingDates(createValidDates())
        .build();
  }
}
```
If a test needs a slightly modified variant, it can easily mutate it immutably:
```java
Booking bookingWithNoFirstname = BookingGenerator.createValidBooking().withFirstName(null);
```
This is inherently thread-safe, shorter, and easier to understand.

---

## 🚀 4. Test Automation Ideas & CI/CD Enhancements

Here are strategic suggestions to scale this framework to support advanced production pipelines:

### ⚡ 1. Parallel Test Execution
Since the framework manages test states using thread-safe components (like `ConcurrentLinkedQueue` in `BookingDetailsPool`) and the Target Restful Booker API is highly concurrency-friendly, you should configure native JUnit 5 parallel test execution in [build.gradle](file:///Users/mzyrkowski/IdeaProjects/restful-booker-test-automation/build.gradle) or `junit-platform.properties`:

```properties
junit.jupiter.execution.parallel.enabled = true
junit.jupiter.execution.parallel.mode.default = concurrent
```
This can cut regression execution times by up to **60-80%**!

### 🔍 2. JSON Schema Contract Validation
Before asserting specific values in the response, you should validate the response **structure** against a JSON schema to ensure API contract integrity. RestAssured has native support for this:

```java
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public void validateBookingResponseContract(Response response) {
  response.then().assertThat().body(matchesJsonSchemaInClasspath("schemas/booking-schema.json"));
}
```
This protects your regression suite from failing due to unexpected changes in API payload types, giving you precise error logs.

### 🎭 3. API Contract Testing (Consumer-Driven Contracts with Pact)
Instead of relying only on live API endpoints (which can be slow, rate-limited, or unstable), you can introduce **Pact** to define consumer-producer contracts. 
* This allows tests to run against mock servers instantly in mock profiles.
* It guarantees that any breaking changes in the Booker API are flagged long before code integration.

---

## 📌 Summary Action Items Matrix

To organize these improvements, refer to the implementation matrix below:

| Feature/Improvement | Core Problem Addressed | Effort | Priority |
| :--- | :--- | :--- | :--- |
| **Separate DTOs from Assertions** | Violation of SRP & Interface Segregation | Medium | **High** |
| **Implement Lent/AutoCloseable Pool** | Entity resource leaks on assertion failures | Low | **High** |
| **Convert to Fluent Assertions** | Verbosely procedural void assertions | Medium | **Medium** |
| **Refactor to Stateless Generators** | Thread-safety risks on stateful builders | Low | **Medium** |
| **Enable Parallel Execution** | Slow, sequential regression executions | Low | **Low** |
| **Introduce JSON Schema Contracts** | Payload structure drifts uncaught by values | Low | **Low** |

This roadmap provides a concrete path to optimizing the maintainability, scalability, and performance of the restful-booker-test-automation framework.
