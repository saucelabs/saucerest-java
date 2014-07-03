SauceREST Java
==============

A java client for Sauce OnDemand's REST API. This supercedes the old `sauce-rest-api <http://repository-saucelabs.forge.cloudbees.com/release/com/saucelabs/sauce-rest-api/>`_.

Using this client you can update Job info, including pass/fail status and
other information supported.

http://saucelabs.com/docs/sauce-ondemand#alternative-annotation-methods

Usage
-----

Examples in https://github.com/saucelabs/saucerest-java/blob/master/src/ExampleUsage.java

Maven:

.. code:: xml

    <dependencies>
      <dependency>
        <groupId>com.saucelabs</groupId>
        <artifactId>saucerest</artifactId>
        <version>1.0.17</version>
        <scope>test</scope>
      </dependency>
    </dependencies>
    
    <repository>
      <id>saucelabs-repository</id>
      <url>https://repository-saucelabs.forge.cloudbees.com/release</url>
      <releases>
        <enabled>true</enabled>
      </releases>
      <snapshots>
        <enabled>true</enabled>
      </snapshots>
    </repository>


Note: The latest version can always be found in `the repository <http://repository-saucelabs.forge.cloudbees.com/release/com/saucelabs/saucerest/>`_.
