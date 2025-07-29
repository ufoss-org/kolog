## Release
* Go to GitHub actions: https://github.com/ufoss-org/kolog/actions
* Execute 'Publish on demand' (no need for build, because it builds before publishing, so it would fail before doing
anything important)
* When finished, go to the publishing page https://central.sonatype.com/publishing (ufoss account)
  * For each deployment, click on **Publish**
  * Refresh again after several minutes, deployment status must be "PUBLISHED"
* do **release** task (for minor release, press Enter for suggested versions: release version = current,
new version = current + 1)

## Publishing
Follow https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/#2-make-separate-requests

curl --request GET --header "Authorization: Bearer *replaceMe*" https://ossrh-staging-api.central.sonatype.com/manual/search/repositories?ip=any&profile_id=org.ufoss
Then call this for each repository ID
curl --request POST --header "Authorization: Bearer *replaceMe*" https://ossrh-staging-api.central.sonatype.com/manual/upload/repository/*repoId*

Before all:
Generate a User Token in https://central.sonatype.org/publish/generate-portal-token/ (ufoss account)

## Signing
Inspired by [this](https://stackoverflow.com/a/66457517)
Followed [this link for CI deploy](https://docs.gradle.org/current/userguide/signing_plugin.html#sec:in-memory-keys)

### Step 1: Extract the secret key
```
gpg --list-secret-keys --keyid-format LONG
gpg --export-secret-keys --armor {your_keyId}
```

### Step 2: Store the extracted GPG key and passphrase as secrets
Follow [this](https://docs.github.com/en/actions/reference/encrypted-secrets)

### Step 3: use env in gradle build
Follow [this](https://github.com/actions/setup-java/tree/v1.4.3#publishing-using-gradle)

## Deprecated local release
* verify the current version you want to release in gradle.properties
* verify you are using SSH with GIT
* use Temurin 11 as project JDK, but Temurin 17 for Gradle
* do **publish** task
