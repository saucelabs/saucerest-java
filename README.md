SauceREST Java
==============

[![codecov.io](https://codecov.io/github/saucelabs/saucerest-java/coverage.svg?branch=master)](https://codecov.io/github/saucelabs/saucerest-java?branch=master)
[![Build Status](https://travis-ci.org/saucelabs/saucerest-java.svg?branch=master)](https://travis-ci.org/saucelabs/saucerest-java)

A java client for Sauce OnDemand’s REST API. This supercedes the old [sauce-rest-api].

Using this client you can update Job info, including pass/fail status and other information supported.

<http://saucelabs.com/docs/sauce-ondemand#alternative-annotation-methods>

Usage
-----
Mark a job passed or failed:
```java
SauceREST r = new SauceREST("username", "access-key");
r.jobPassed(job_id);
r.jobFailed(job_id);
```

Get all tunnels:

```java
SauceREST r = new SauceREST("username", "access-key");
String tunnels = r.getTunnels();
```

Save the Selenium server log to your filesystem:

```java
SauceREST r = new SauceREST("username", "access-key");
r.downloadLog("job_id", "/var/tmp/selenium.log");
```

Save the HAR File from a test using extended debugging:

```java
SauceREST r = new SauceREST("username" "access-key");
r.downloadHAR("job_id", "/var/tmp/HAR.log");
```

Maven
-----

```xml
<dependencies>
  <dependency>
    <groupId>com.saucelabs</groupId>
    <artifactId>saucerest</artifactId>
    <version>LATEST VERSION</version>
    <scope>test</scope>
  </dependency>
</dependencies>
```

For latest version please check the following link: https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.saucelabs%22%20AND%20a%3A%22saucerest%22

