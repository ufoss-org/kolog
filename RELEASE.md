## Release
* Go to github actions : https://github.com/ufoss-org/kolog/actions
* Execute 'Publish on demand'
* When finished, go to staging repos : https://s01.oss.sonatype.org/#stagingRepositories (ufoss account)
  * do **close** , refresh
  * then **release**
  * refresh again after several seconds, repo has gone -> it's released on maven central
* do **release** task locally (for minor release, press Enter for suggested versions : release version = current, new version = current + 1)

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
* use openJDK 11 as project JDK
* do **publish** task
