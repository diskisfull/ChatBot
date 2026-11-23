# ChatBot Project Roadmap

A living checklist for this project. Update it as work is completed.

## Purpose

The chatbot itself is deliberately simple. **The real goal is to learn build systems, CI/CD, deployment, DevOps practices, and observability** by building and shipping a Java service end to end.

## Working agreements

- **Learn by typing.** Commands and code are typed by hand, not blindly pasted, and each step is explained before moving on.
- **Everything goes through a pull request.** No direct pushes to `main`. The `main` branch is protected by a ruleset that requires the `build` check to pass.
- **Pin your tools.** The Maven version (wrapper), JDK (Temurin 25), and Action versions are all explicit so builds are reproducible.
- **Public repo hygiene.** No secrets, personal emails, or machine-specific paths in commits. Use GitHub Secrets for credentials when needed.
- **One concern per PR.** Small, reviewable changes.

## Current stack and decisions

| Area | Choice | Why |
|---|---|---|
| Language / JDK | Java 25 (Eclipse Temurin) | Latest LTS; free to use; supported by `setup-java` and Docker Hub images |
| Build tool | Maven with Maven Wrapper (`./mvnw`) | Declarative, explicit lifecycle; wrapper pins the Maven version for everyone, including CI |
| Tests | JUnit 5 (Jupiter) | Standard; run by Surefire during `verify` |
| CI | GitHub Actions (`.github/workflows/ci.yml`) | Runs on every PR and on pushes to `main` |
| Framework (next) | Spring Boot (proposed) | Industry standard; Actuator and Micrometer give health and metrics endpoints with little code |

## Progress

### Phase 1: Project and build system: done
- [x] Create Maven project in IntelliJ (Java 25, Temurin)
- [x] Understand `pom.xml`, coordinates, and the default lifecycle (`clean`, `compile`, `test`, `package`)
- [x] Add Maven Wrapper (`mvnw`) and understand why it exists
- [x] Build a jar and run it from the command line
- [x] `.gitignore` covers `target/` and IDE files (`.idea/`)

### Phase 2: Git and GitHub: done
- [x] Repo created and pushed to GitHub (public)
- [x] Branch and pull request workflow
- [x] Practice `git revert` (undo a merged PR without rewriting history), and revert of a revert
- [x] Learned that `git log` shows only local knowledge; `gh pr list` and `git branch -r -vv` show remote truth

### Phase 3: Tests: done
- [x] `ChatBot` class with simple reply logic
- [x] JUnit 5 dependency (`test` scope) and unit tests
- [x] Observed a deliberate test failure to recognize `BUILD FAILURE`

### Phase 4: Continuous Integration: done
- [x] GitHub Actions workflow: checkout, `setup-java` (Temurin 25), Maven dependency cache, `./mvnw clean verify`
- [x] Read workflow logs; compared cold vs warm cache run times (about 25s vs 14s)
- [x] README added via PR (watched both `pull_request` and `push` triggers)
- [x] Ruleset on `main`: PR required, `build` check required, force pushes blocked
- [x] Verified the gate: a PR with a failing test cannot be merged

### Phase 5: Turn the bot into a service (next)
- [ ] Decide framework (Spring Boot proposed) and confirm
- [ ] Add Spring Boot parent and web starter to `pom.xml`
- [ ] `POST /chat` endpoint backed by `ChatBot`
- [ ] Tests for the endpoint
- [ ] Run locally and call it with `curl`
- [ ] Executable jar via the Spring Boot Maven plugin

### Phase 6: Harden the pipeline
- [ ] Upload the built jar as a workflow artifact
- [ ] Publish test reports in CI
- [ ] `concurrency` to cancel superseded runs
- [ ] Optional `paths-ignore` for docs-only changes (and discuss the trade-off)
- [ ] Dependabot for Maven dependencies and Action versions
- [ ] Code coverage (JaCoCo)

### Phase 7: Containers
- [ ] Multi-stage `Dockerfile` (build with a JDK image, run on a slim JRE image)
- [ ] Pin base image tags; run as a non-root user
- [ ] Build and run the image locally
- [ ] Build the image in CI
- [ ] Publish to GitHub Container Registry (GHCR)

### Phase 8: Releases
- [ ] Semantic versioning; replace `1.0-SNAPSHOT` with a release flow
- [ ] Tag-triggered release workflow (`v*` tags)
- [ ] Generate release notes and publish a GitHub Release with the jar attached
- [ ] Push versioned and `latest` Docker tags

### Phase 9: Deployment (CD)
- [ ] Choose a target (e.g. a free-tier container host)
- [ ] Deploy from CI using GitHub Secrets and environments
- [ ] Manual approval for production
- [ ] Rollback procedure (re-deploy previous tag)

### Phase 10: Observability
- [ ] Health checks (liveness and readiness)
- [ ] Metrics via Micrometer and a Prometheus endpoint
- [ ] Custom metrics (request counts, latency, reply types)
- [ ] Prometheus + Grafana locally via Docker Compose
- [ ] Structured logging with correlation IDs
- [ ] Pipeline metrics: build duration, failure rate, deploy frequency, lead time (DORA)
- [ ] Alerting basics (what to alert on, and why)

### Phase 11: Stretch
- [ ] CodeQL and dependency vulnerability scanning
- [ ] SBOM generation
- [ ] Reproducible-build checks
- [ ] Smarter bot logic (optionally an LLM backend, with the API key kept in Secrets)

## Key commands

```bash
./mvnw clean verify          # full build and tests (what CI runs)
./mvnw clean package         # build the jar
git switch -c <branch>       # start a change
gh pr create --base main     # open a PR
gh pr checks --watch         # follow CI live
gh run list --limit 5        # recent workflow runs
```

## Lessons learned so far

- A build that runs zero tests is green but proves nothing. Check the test count, not just the colour.
- The build must work from a clean checkout using only files in Git. No IDE dependency.
- `git revert` undoes a change by adding a new commit. It does not rewrite shared history.
- Rewriting history does not remove old commits from a hosted repo (pull request refs keep them). To truly purge something, recreate the repo.
- A workflow file only runs if it exists on the branch the PR is built from.
- Warm dependency caches meaningfully cut CI time.
- Required status checks turn CI from advice into a gate, and the gate should be tested by deliberately failing it.

## Resuming in a new chat

If the conversation context is reset, give the assistant this file and say something like:

> I'm working through `docs/ROADMAP.md` in this repo. Act as a step-by-step tutor. I type commands myself and want each step explained. We're on the first unchecked item. Check the repo's real state (`git status`, `gh pr list`) before assuming anything.
