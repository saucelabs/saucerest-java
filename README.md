<p align="center">
  <a href="https://saucelabs.com/sign-up">
    <img alt="saucerest-java Logo" src="https://raw.githubusercontent.com/saucelabs/saucerest-java/master/.github/SauceLabsLogo.png">
  </a>
</p>

<h1 align="center"><strong>saucerest-java</strong></h1>

<p align="center">
  <a href="https://github.com/saucelabs/saucerest-java/releases">
    <img alt="GitHub release (latest by date)" src="https://img.shields.io/github/v/release/saucelabs/saucerest-java?style=for-the-badge&logo=github&logoColor=white">
  </a>
  <a href="https://github.com/saucelabs/saucerest-java/actions/workflows/java-ci.yml">
    <img alt="CI/CD" src="https://img.shields.io/github/actions/workflow/status/saucelabs/saucerest-java/java-ci.yml?branch=master&label=CI/CD&style=for-the-badge&logo=githubactions&logoColor=white">
  </a>
  <a href="https://codecov.io/gh/saucelabs/saucerest-java">
    <img alt="Codecov" src="https://img.shields.io/codecov/c/gh/saucelabs/saucerest-java?style=for-the-badge&logo=codecov&logoColor=white">
  </a>
  <a href="https://central.sonatype.com/artifact/com.saucelabs/saucerest">
    <img alt="Maven" src="https://img.shields.io/maven-central/v/com.saucelabs/saucerest?style=for-the-badge&logo=maven&logoColor=white">
  </a>
</p>
<hr>

A Java client for Sauce Labs REST API.

Currently, this client only support the endpoints listed [here](https://docs.saucelabs.com/dev/api/).

With version 2.x of SauceREST we have made code-breaking changes to this wrapper. It has been updated to be more
future-proof and to also support the newest APIs from Sauce Labs.

This is going to be continuous process which means we will release changes to SauceREST over time.

If a function you're after isn't supported, we suggest either shelling out and using the curl version, _or_ sending a pull
request!  [Contribution Details Here](https://github.com/saucelabs/saucerest-java/blob/master/CONTRIBUTING.md).

<hr>

# How to use

<details open>
  <summary>Creating a client object</summary>

```java
SauceREST sauceREST=new SauceREST("username","access-key",DataCenter.EU_CENTRAL);
```

</details>

Parameters:

| Name        | Type                            | Details                                                     |
|-------------|---------------------------------|-------------------------------------------------------------|
| username    | String (required)               | Your sauce labs username                                    |
| access-key  | String (required)               | Your sauce labs accesskey                                   |
| data_center | String or DataCenter (required) | One of `US_WEST`, `US_EAST`, `EU_CENTRAL`, `APAC_SOUTHEAST` |

## Code examples

The best way to find out how to use this library is to look at the tests. They are located in the `src/test/java` directory. Especially the integration tests
will provide you with a good overview of how to use this library.

# Maven

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

For latest version please check the following link: [click](https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.saucelabs%22%20AND%20a%3A%22saucerest%22).

## Contributing

Check out our contribution guide [here](CONTRIBUTING.md) for details.

Want a fast, setup dev
environment?  [![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/saucelabs/saucerest-java)

### Hacktoberfest

Here for [Hacktoberfest?](https://hacktoberfest.com/). Check
out [Our Hacktoberfest Issues](https://github.com/saucelabs/saucerest-java/issues?q=is%3Aissue+is%3Aopen+label%3Ahacktoberfest), or
visit [https://opensource.saucelabs.com/hacktoberfest/](https://opensource.saucelabs.com/hacktoberfest/) to see all our Hacktoberfest projects!