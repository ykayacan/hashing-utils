# Releasing

1. Change the version in `gradle.properties` to a non-SNAPSHOT version.
2. `git commit -am "chore: release X.Y.Z"` (where X.Y.Z is the new version)
3. `git tag -a X.Y.Z -m "Version X.Y.Z"` (where X.Y.Z is the new version)
4. Update the `gradle.properties` to the next SNAPSHOT version.
5. `git commit -am "chore: prepare next development version"`
6. `git push && git push --tags`