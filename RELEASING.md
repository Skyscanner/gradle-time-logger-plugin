# Releasing
Are you ready to show the world the new and improved things you have merged into `master`?

## Bump the plugin version
Bump the `pluginVersion` defined in [`gradle.properties`](gradle.properties). Please respect [Semantic Versioning](https://semver.org/) conventions.

## Tag the release commit with the release version

E.g. for release `1.0.0` the tag would be `v1.0.0` and the git command would look like this:

```shell
git tag v1.0.0
```

Don't forget to push your tags:

```shell
git push --tags
```

## Making the release available in Gradle Plugin Portal
CircleCI is setup so you only have to merge your tagged commit to the `master` branch. 
This requires that the right secrets are present in the build environment:

- `GRADLE_PUBLISH_KEY`
- `GRADLE_PUBLISH_SECRET`

These can be added/edited by accessing [the CircleCI console](https://circleci.com/gh/Skyscanner/gradle-time-logger-plugin/edit#env-vars) with the right permissions.
