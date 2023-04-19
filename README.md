# JXAPI

A wrapper for Cryptocurrency Exchanges API written in Java.

## Introduction
Most popular crypto exchanges like ByBit, Binance... expose APIs for trading.
These APIs usually consist in REST endpoints, and websocket endpoints to get notified in real time of market price, account balance, orders matching.
A few of them already provide a wrapper written in Java. However, those wrappers may not be up to date, or lack robustness, for instance regarding websocket failure management.
This project aims to provide alternative for these wrappers uusing the following principes:
 - Provide generic wrappers for REST API calls, websocket management.
 - Those wrappers also provide hooks to perform exchange specific logic like signing requests of private APIs
 - Requests and response objects are mapped to custom classes using code generation.
 - Code generation parses an input JSON file describing all the APIs with endpoints, request/responses messages and their fields written using exchange API documentation, and generate Java interfaces for these APIs

 ## Supported exchanges
TODO! Currently under development :)

