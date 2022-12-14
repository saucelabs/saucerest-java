SauceREST Java [![codecov.io](https://codecov.io/github/saucelabs/saucerest-java/coverage.svg?branch=master)](https://codecov.io/github/saucelabs/saucerest-java?branch=master)
![example workflow](https://github.com/saucelabs/saucerest-java/actions/workflows/java-ci.yml/badge.svg)

---

A Java client for Sauce Labs REST API.

Using this client you can update Job info, including pass/fail status and other information supported.  Only the Sauce Labs Virtual Device Cloud is supported.  For Real Devices, please see [the docs](https://api.testobject.com/).

If a function you're after isn't supported, we suggest either shelling out and using the curl version, _or_ sending a pull request!  [Contribution Details Here](https://github.com/saucelabs/saucerest-java/blob/master/CONTRIBUTING.md).

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

| Name        | Type                            | Details                                |
|-------------|---------------------------------|----------------------------------------|
| username    | String (required)               | your sauce labs username               |
| access-key  | String (required)               | your sauce labs accesskey              |
| data_center | String or DataCenter (required) | one of US, US_EAST, EU, APAC_SOUTHEAST |



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

Filenames are set automatically to `jobId_yyyyMMdd_HHmmSS_filename.ext`, where yyyyMMdd_HHmmSS is the retrieval timestamp and ext is determined by the asset type.

Each of these methods comes in two flavours; the `downloadX` methods which ignore exceptions, and the `downloadXOrThrow` methods which throw:
 * SauceException.NotAuthorized if credentials are missing or wrong
 * FileNotFound if resources are missing or don't exist
 * IOException if any of the many networking horrors which can occur, do

The `downloadX` methods return a `boolean` indicating if the download was successful.
### Selenium log

```java
// Download the log; Ignore exceptions
sauce.downloadLog("job_id", "/var/tmp/");  // => true if the Log downloads correctly

// Download the log; Raise SauceException.NotAuthorized, FileNotFound, IOException
sauce.downloadLogOrThrow("job_id", "/var/tmp");
```

Extension: `.log`

### HAR File
HAR files are only available for jobs using [Extended Debugging](https://docs.saucelabs.com/insights/debug/).

```java
// Download the HAR file; Ignore exceptions
sauce.downloadHAR("job_id", "/var/tmp/"); // => true if the HAR File downloads correctly

// Download the HAR file; Raise SauceException.NotAuthorized, FileNotFound, IOException
sauce.downloadHAROrThrow("job_id", "/var/tmp");
```

Extension: `.har`

### Video
Video is only available for jobs which have not [disabled video recording](https://docs.saucelabs.com/dev/test-configuration-options/#TestConfigurationOptions-Disablevideorecording.

```java
// Download the Log; Ignore exceptions
sauce.downloadVideo("job_id", "/var/tmp"); // => true if the Video downloads correctly

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
sauce.getJobInfo(n);  #n is the number of jobs to retrieve,as an integer
```

### About your tunnels

```java
String tunnels=sauce.getTunnels();
```

# SauceREST 2.x

With version 2.x of SauceREST we have made code-breaking changes to this wrapper. It has been updated to be more
future-proof and to also support the newest APIs from Sauce Labs.

This is going to be continuous process which means we will release changes to SauceREST over time.

## Storage

### Get all files

```java
Storage storage = new SauceREST(DataCenter.EU).getStorage();
GetAppFiles getAppFiles = storage.getFiles();
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

For latest version please check the following link: https://search.maven.org/#search%7Cgav%7C1%7Cg%3A%22com.saucelabs%22%20AND%20a%3A%22saucerest%22.


## Contributing
Check out our contribution guide [Here](CONTRIBUTING.md) for details.

Want a fast, setup dev environment?  [![Open in Gitpod](https://gitpod.io/button/open-in-gitpod.svg)](https://gitpod.io/#https://github.com/saucelabs/saucerest-java).

### Hacktoberfest
Here for [Hacktoberfest?](https://hacktoberfest.com/).  Check out [Our Hacktoberfest Issues](https://github.com/saucelabs/saucerest-java/issues?q=is%3Aissue+is%3Aopen+label%3Ahacktoberfest), or visit [https://opensource.saucelabs.com/hacktoberfest/](https://opensource.saucelabs.com/hacktoberfest/) to see all our Hacktoberfest projects!