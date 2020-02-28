#SauceREST Java

[![codecov.io](https://codecov.io/github/saucelabs/saucerest-java/coverage.svg?branch=master)](https://codecov.io/github/saucelabs/saucerest-java?branch=master)
[![Build Status](https://travis-ci.org/saucelabs/saucerest-java.svg?branch=master)](https://travis-ci.org/saucelabs/saucerest-java)

A Java client for Sauce Labs’s REST API. This supercedes the old [sauce-rest-api].

Using this client you can update Job info, including pass/fail status and other information supported.  Only the Sauce Labs Virtual Device Cloud is supported.  For Real Devices, please see [the docs](https://api.testobject.com/).

If a function you're after isn't supported, we suggest either shelling out and using the curl version, _or_ sending a pull request!  [Contribution Details Here](https://github.com/saucelabs/saucerest-java/blob/master/contributing.md)

<http://saucelabs.com/docs/sauce-ondemand#alternative-annotation-methods>

# Usage
## Create an instance of the client
```java
SauceREST sauce = new SauceREST("username", "access-key", "data_center");
```
OR
```java
SauceREST sauce = new SauceREST("username", "access-key", Datacenter.US);
```
Paramaters: 

| Name        | Type              | Details       |
| ----------- | ----------------- | ------------- |
| username    | String (required) | your sauce labs username  |
| access-key  | String (required) | your sauce labs accesskey  |
| data_center  | String (optional) | one of US, US_EAST, EU (Default US)  |



Users generally only have access to their own jobs and resources.  Parent accounts may have access to their children's jobs.  Check out the Sauce Labs documentation for more information.

## Change a job's metadata
### Mark a job passed or failed:
```java
sauce.jobPassed(job_id);
sauce.jobFailed(job_id);
```

## Get all tunnels

```java
String tunnels = sauce.getTunnels();
```

## Download Assets
Assets are downloaded by providing a job ID and the location to save the asset to.

Filenames are set automatically to `job_idyyyyMMdd_HHmmSS.ext`, where yyyyMMdd_HHmmSS is the retrieval timestamp and ext is determined by the asset type.

Each of these methods comes in two flavours; the `downloadX` methods which ignore exceptions, and the `downloadXOrThrow` methods which throw:
 * SauceException.NotAuthorized if credentials are missing or wrong
 * FileNotFound if resources are missing or don't exist
 * IOException if any of the many networking horrors which can occur, do

See (Asset Fetching)[https://github.com/DylanLacey/saucerest-java/wiki/Asset-Fetching] for more details.

### Selenium log

```java
// Download the log; Ignore exceptions
sauce.attemptLogDownload("job_id", "/var/tmp/");

// Download the log; Raise SauceException.NotAuthorized, FileNotFound, IOException
sauce.downloadLogOrThrow("job_id", "/var/tmp");
```

Extension: `.log`

### HAR File
HAR files are only available for jobs using [Extended Debugging](https://wiki.saucelabs.com/pages/viewpage.action?pageId=70072943).

```java
// Download the HAR file; Ignore exceptions
sauce.attemptHARDownload("job_id", "/var/tmp/");

// Download the HAR file; Raise SauceException.NotAuthorized, FileNotFound, IOException
sauce.downloadHAROrThrow("job_id", "/var/tmp");
```

Extension: `.har`

### Video
Video is only available for jobs which have not [disabled video recording](https://wiki.saucelabs.com/display/DOCS/Test+Configuration+Options#TestConfigurationOptions-Disablevideorecording).

```java
// Download the Log; Ignore exceptions
sauce.attemptVideoDownload("job_id", "/var/tmp");

// Download the Log; Raise SauceException.NotAuthorized, FileNotFound, IOException
sauce.downloadVideoOrThrow("job_id", "/var/tmp");
```

Extension: `.mp4`

## Get Information
### About a particular job
```java
sauce.getJobInfo("job_id");
```

### About the last 20 jobs
```java
sauce.getJobInfo();
```

### About a requested number of jobs
```java
sauce.getJobInfo(n);  #n is the number of jobs to retrieve, as an integer
```

### About your tunnels
```java
String tunnels = sauce.getTunnels();
```

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

For latest version please check the following link: https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.saucelabs%22%20AND%20a%3A%22saucerest%22

# Contributing
Check out our contribution guide [Here](https://github.com/saucelabs/saucerest-java/blob/master/contributing.md).

