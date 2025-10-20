## 1. System Under Test (SUT)
The purpose of api test automation framework is to test
REST api service: https://restful-booker.herokuapp.com

documentation: https://restful-booker.herokuapp.com/apidoc

## 2. Tools
- Java 21
- Spring Boot
- Gradle
- Rest Assured
- JUnit 5
- Allure Report
- Lombok

## 3. Test execution

### 3.1 Regression test execution
`gradle test`
<br/>

or

`gradle junit5 -Ptag=regression`

Will execute full regression

### 3.2 Running arbitrary tests
It is possible to run any test or tests which are annotated by JUnit5 tags.
For this purpose gradle task `junit5` was introduced.
<br/>
To run tests which include specific task e.g. `"debug"`,
annotate test/s by `@Tag("debug")`, then run the following command:
`gradle junit5 -Ptag=debug`
<br/>
> Note: If `-Ptag=` option not provided, full regression will be run
