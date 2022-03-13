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

Before running the app, you need to set the appropriate config. See [Configs section](#configs).

##### IDEA

Go to `api/src/main/scala/io/santa/web/elves/Main.scala` and press the green triangle.

##### Terminal (sbt)

```shell
sbt api/run
```

This will start an API server available at http://localhost:8956, if you copied the config from [Configs section](#configs).

### Build

#### IDEA

Just press the green build hummer.

#### Terminal (sbt)

```shell
sbt compile
```

### Test

```shell
sbt test
```

### Lint

#### Run scalafix

```shell
sbt scalafixAll
```

### Format code

#### Run scalafmt

See the [official documentation](https://scalameta.org/scalafmt/docs/installation.html) on how to configure scalafix to format files on save.

Format all files in the project manually:
```shell
sbt scalafmtAll
```

## Project details

The project uses [ZIO](https://zio.dev) effect system to structure the application code and implement asynchronous backend safely and efficiently.

### API

The project uses [tapir](https://tapir.softwaremill.com/en/latest/index.html) to define the endpoints and then interprets the definition to [zio-http](https://github.com/dream11/zio-http) server.

### Configs

All the config needed to start the API resides in `application.conf`. To facilitate local development you can also create a file `applicaiton.local.conf` and override the values from the original config. The `applicaiton.local.conf` must not be committed (and is already ignored by default).

To see what a config value means it is recommended to see how the config is used in `api/src/main/scala/io/santa/web/elves/Main.scala`.

### Database interaction

The app is designed to connect to postgres and run queries against it.

#### Quill

[Quill](https://github.com/zio/zio-quill) is used to write queries in the code and execute them. Quill queries are compiled to Postres SQL queries and then executed.

Under the hood it uses [Hikari Connection Pool](https://github.com/brettwooldridge/HikariCP) to manage database connection.

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
