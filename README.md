# exchange-delta-service

[![build](https://github.com/wexom/exchange-delta-service/actions/workflows/maven.yml/badge.svg)](https://github.com/wexom/exchange-delta-service/actions/workflows/maven.yml/badge.svg)

The task from Bank ID for position interview.

### Objective:
Create a Spring Boot application (using Kotlin or optionally Java) with the following features:

- **Self-contained Service**: The application should function as an independent service without runtime dependencies, unless specified otherwise. Bonus points if non-blocking I/O is used.
- **Currency Exchange Rate Functionality**: The service should provide exchange rate information based on data from the Czech National Bank (ČNB) [daily rate TXT file](https://www.cnb.cz/cs/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.txt) or [XML file](https://www.cnb.cz/cs/financni_trhy/devizovy_trh/kurzy_devizoveho_trhu/denni_kurz.xml), along with an external API selected from [this GitHub list of public APIs](https://github.com/public-apis/public-apis?tab=readme-ov-file#currency-exchange).
- **API Implementation**: The application should expose two APIs:
    - **Supported Currency Pairs List**: Returns a list of supported currency pairs.
    - **Exchange Rate Comparison**: For a given currency pair, returns the rate difference between ČNB and the selected provider.
- **Health Check Endpoint**: The service should provide a health check endpoint.
- **Security**: Secure the service using either Basic Authentication or OAuth (using Google as the OAuth server if applicable).

### What could be improved:
- **Error Handling**: The application should handle errors gracefully and return appropriate HTTP status codes, for example for client responses
- **Logging**: The application should log important events and errors
- **Testing**: The application should have more unit tests and integration tests
- **Cache**: The application should cache the exchange rate data
- **Dynamic provider loading**: The application should allow for dynamic loading of exchange rate providers