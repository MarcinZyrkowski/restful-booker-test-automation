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
`./gradlew test`
<br/>

or

`./gradlew test -PincludeTags=regression`

Will execute full regression

### 3.2 Running arbitrary tests
It is possible to run any test or tests which are annotated by JUnit5 tags.
<br/>
To run tests which include specific task e.g. `"debug"`,
annotate test/s by `@Tag("debug)` 
(or `@Debug` - especially prepared alias for investigation), then 
run the following command:
`./gradlew test -PincludeTags=debug`
<br/>

## 4. View the report
After test execution is completed, to view Allure report:
1. Open terminal at folder: `reports`
2. execute the following commands:
`allure open allure-report/allureReport`
