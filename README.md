SauceREST Java
==============

[![codecov.io](https://codecov.io/github/saucelabs/saucerest-java/coverage.svg?branch=master)](https://codecov.io/github/saucelabs/saucerest-java?branch=master)
[![Build Status](https://travis-ci.org/saucelabs/saucerest-java.svg?branch=master)](https://travis-ci.org/saucelabs/saucerest-java)

A java client for Sauce OnDemand’s REST API. This supercedes the old [sauce-rest-api].

Using this client you can update Job info, including pass/fail status and other information supported.

<http://saucelabs.com/docs/sauce-ondemand#alternative-annotation-methods>

Usage
-----

```java
SauceREST r = new SauceREST("username", "access-key");
String tunnels = r.getTunnels();
```


Maven
-----

```xml
<dependencies>
  <dependency>
    <groupId>com.saucelabs</groupId>
    <artifactId>saucerest</artifactId>
    <version>1.0.27</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```
