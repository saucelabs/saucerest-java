# Thank You!
We appreciate contributions from the community, helping to make this library better for everyone who uses it.

# Developing new features and fixing bugs
## Step 1. Get You A Copy
1. Fork this repository
2. Clone your fork to your local machine
3. (Optional but helpful) Create a branch for your changes with `git checkout -b branchname`.  Make your branchname short but descriptive, eg `add_contribution_doc`

## Step 2. Set up your environment
1. Make sure you've a JDK installed, along with Maven, and all the appropriate ENV Vars set

## Step 3. Make your changes
1. Code Changes should be accompanied by documentation (if new features) and tests
3. Run the JUnit tests with `mvn test` if appropriate
4. Once they all pass, you're gold!

## Step 4. Commit
1. List changed files with `git status`
2. Add relevant changed files to your commit with `git add filename`
3. Start your commit with `git commit`
4. Write a good commit message.  Good messages have a short title describing what changed, as well as a longer message summarising the changes with any context required. If fixing a lodged issue, mention this in the title
5. Try to commit atomic bug fixes or features, or at least a related set of features

## Step 5. Send a Pull Request
1. `git push` to send your commands back to Github
2. Visit your repo on Github and click then `Pull Request` button
3. If required, fill in a PR request.  Treat it like a commit message; A short title describing what changed, as well as a longer message summarising changes with any context required.  If fixing a lodged issue, include this in the title
4. Send the Pull Request

# Using a locally build version for Development or to resolve a bug
We highly encourage users to send PRs and updates that the community might find useful.  However, you might want to use a personal release if you're waiting for fixes to be built into a release, to test complicated behaviour, or for company-specific changes.

One way of doing this is to take a copy of the source and install it locally by doing
`mvn install`
from the repository root.

You can then update the pom file for your target project so that you're checking out your build version:

```
<dependencies>
        <dependency>
            <groupId>com.saucelabs</groupId>
            <artifactId>saucerest</artifactId>
            <version>1.0.38-SNAPSHOT</version>
        </dependency>
</dependencies>
```

Get the value of the VERSION from the version key in the saucerest-java pom file.  ATM it's on line 6: `<version>1.0.38-SNAPSHOT</version>`.

You may need to give it a custom version.

# Releasing
## Configuration
Releases are done using the Github Action `Release to Maven Central`.  Github Secret Setup is required in advance:

### Maven Credentials
Configure `MAVEN_USERNAME` and `MAVEN_CENTRAL_PASSWORD` with credentials of a user with access to the com.saucelabs group

### GPG Private Key
GPG Private Key must be exported in ascii armor format and stored as `GPG_PRIVATE_KEY`.  The passphrase must be store in `GPG_PASSPHRASE`.

## Performing a release -- Github Action
1. Choose the "Release to Maven Central" workflow action
2. Choose a releaseType -- This decides what version number will be incremented for this release
3. Set `dryRun` to `false` to perform an actual release

## Release Details
### What Happens -- Always
1. The library is built
2. Tests run
3. Maven advances the version number according to `releaseType` and removes `SNAPSHOT`, then packages
4. Artifacts are GPG Signed

### What Happens when not dry-run
5. Code is tagged and committed
6. Maven prepares for next release by advancing version number according to `releaseType` and appending `SNAPSHOT`
7. Artifacts are released to Maven Central