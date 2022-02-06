Backend for Secret Santa website, which is a project for SQR 2022 course at Innopolis University.

# Development

`master` branch has the latest changes and must always be buildable. All the code changes are done through sending pull requests to the repositories. After [CI](#ci) has successfully finished and a reviewer has approved changes, the code can be merged to the `master` branch.

## Local environment recommendations

- Use Temurin JDK 17.0.2

If you are using IntelliJ IDEA:
- Install [ZIO for IntelliJ](https://plugins.jetbrains.com/plugin/13820-zio-for-intellij)

## Common actions on the project

### Run

#### Run API

- [ ] TODO

### Build

- [ ] TODO

### Test

- [ ] TODO

### Lint

#### Run scalafix

- [ ] TODO

### Format code

#### Run scalafmt

- [ ] TODO

## Project details

- [ ] TODO: the project uses tapir to define API and interprets the description into zio-http server

## CI

When a pull request is sent to `master` or a branch with a pull request to `master` is pushed, the following checks will run via GitHub Actions:

- Build — all projects in the repository are built to check that the code compiles
- Test — all tests are run to check that new changes have not broken the existing functionality
- Lint — run scalafix. If it fails run `sbt scalafixAll` and fix issues that are not autofixable manually

  [scalafix official documentation](https://scalacenter.github.io/scalafix/docs/users/installation.html#sbt) tells that SemanticDB compiler plugin with `-Yrangepos` flag adds overhead to the compilation, so it is recommended to create a local `.jvmopts` file with the following content:
  ```
    -Xss8m
    -Xms1G
    -Xmx8G
  ```
- Check code formatting — the code formatting will be checked via `scalafmt`. If it fails run `sbt scalafmtAll` and commit the updated formatting.

For more information, see `.github/workflows/ci.yml`.
