# REST API Test Framework  
**Java 21 • TestNG • Rest-Assured • Allure**

Lightweight and production-ready framework for REST API testing with **parallel execution**, **stable specs**, **auto-cleanup**, **retry logic**, and **detailed reporting**.

---

## 🚀 Quick Start

```bash
git clone <your-repo-url>
cd <project-folder>
./gradlew clean test
./gradlew allureServe   # open local report
```

**Requirements:** JDK 21, internet access for Maven Central.

---

## 📦 Artifacts & Reports

- **Allure results** → `build/allure-results`  
- **Allure HTML report** → `build/allure-report` (`./gradlew allureReport`)  
- **Cleanup logs** → `build/logs/cleanup.log` (rotated daily)  

---

## ⚙️ Configuration

Default config file: `config/appConfig.yaml`  

```yaml
baseUrl: "http://3.68.165.45"
connectionTimeoutMs: 3000
readTimeoutMs: 5000
logHttp: true
userDelete: true
editor: "supervisor"
defaultHeaders:
  Accept-Language: "en-US"
```

- `baseUrl` – API root  
- `logHttp` – enable/disable extra HTTP logs  
- `userDelete` – auto-delete created players  
- `editor` – who performs cleanup  

Custom config could be configured this way, but for now it is only one, so this is just an example:  
```bash
./gradlew test -Dconfig.path=env/stage.yaml
```

---

## 🧪 Test Execution

Basic run:  
```bash
./gradlew clean test
```

Parallel execution:  
```bash
./gradlew test -PtestngParallel=methods -PtestngThreads=6
```

Run specific tests:  
```bash
./gradlew test --tests "com.spribe.tests.player_controller_tests.CreatePlayerTests"
./gradlew test --tests "*CreatePlayerTests.validateAdminCreatedWithSupervisorEditor"
```

---

## 🧹 Auto-Cleanup

- Tracks created players per test  
- If `userDelete: true` → deletes them in `@AfterMethod`  
- Logs to `build/logs/cleanup.log`  

Statuses:  
- `OK delete id=...` – deleted  
- `SKIP ... not found (404)` – already gone  
- `WARN ... unexpected status=...` – unexpected response  
- `FAIL ...` – error  

---

## 🗂 Project Structure

```
com.spribe.config        # AppConfig, YAML loader
com.spribe.http.*        # Rest-Assured specs & filters
com.spribe.steps         # Test steps (with @Step)
com.spribe.tests         # BaseTest + TestNG suites
com.spribe.cleanup       # Per-test registry for cleanup
com.spribe.listeners     # Retry analyzer
com.spribe.model.*       # DTOs
```

---

## 🧭 Key Features

- **Java 21 + Gradle** (toolchain)  
- **TestNG** with retry (1)  
- **Rest-Assured** unified spec  
- **Allure integration** with HTTP attachments  
- **Custom logging** (HTTP + cleanup logs)  
- **Data-driven tests** (TestNG providers)  

---

## 📊 Example Test Coverage

- `CreatePlayerTests` → positive/negative (roles, gender, age, password, etc.)  
- `UpdatePlayerTests` → conflicts, invalid values  
- `DeletePlayerTests` → role-based permissions (SUPERVISOR/ADMIN/USER)  
- `GetAllPlayersTests`, `GetPlayerByIdTests` → basic GET flows  

---

## 🔧 Tips

- **Empty Allure report?** → check `build/allure-results`  
- **Too noisy logs?** → set `logHttp: false` in YAML or `-DlogHttp=false`  
- **Keep test data** → set `userDelete: false` in config  
