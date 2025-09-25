package com.spribe.tests;

import com.spribe.cleanup.CreatedPlayersRegistryPerTest;
import com.spribe.config.AppConfig;
import com.spribe.config.ConfigLoader;
import com.spribe.http.spec.RequestSpecFactory;
import com.spribe.http.spec.RequestSpecProvider;
import com.spribe.steps.PlayerSteps;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

public abstract class BaseTest {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final Logger cleanupLog = LoggerFactory.getLogger("CLEANUP");

    protected static AppConfig CFG;

    @BeforeSuite
    public void beforeSuite() {
        CFG = ConfigLoader.load();
        RequestSpecProvider provider = RequestSpecFactory.provider(CFG, System.out);
        RestAssured.requestSpecification = provider.get();
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeTest(Method m) {
        CreatedPlayersRegistryPerTest.clear();
        log.info("=== START TEST: {} ===", m.getName());
    }

    @AfterMethod(alwaysRun = true)
    public void afterTest(Method m, ITestResult r) {
        if (CFG != null && CFG.isUserDelete()) {
            try {
                var ids = CreatedPlayersRegistryPerTest.drainAll();
                if (!ids.isEmpty()) {
                    String editor = CFG.getEditor();
                    var steps = new PlayerSteps();

                    for (Integer id : ids) {
                        try {
                            var resp = steps.deletePlayer(editor, id);
                            int sc = resp.statusCode();
                            if (sc == 204 || sc == 200) {
                                cleanupLog.info("OK delete id={} (status={})", id, sc);
                            } else if (sc == 404) {
                                cleanupLog.info("SKIP id={} not found (404)", id);
                            } else {
                                cleanupLog.warn("WARN id={} unexpected status={}", id, sc);
                            }
                        } catch (Exception e) {
                            cleanupLog.error("FAIL id={} -> {}", id, e.toString());
                        }
                    }
                }
            } catch (Exception e) {
                cleanupLog.error("Cleanup error: {}", e.toString());
            } finally {
                CreatedPlayersRegistryPerTest.clear();
            }
        }


        String outcome = switch (r.getStatus()) {
            case ITestResult.SUCCESS -> "PASSED";
            case ITestResult.FAILURE -> "FAILED";
            case ITestResult.SKIP -> "SKIPPED";
            default -> "UNKNOWN";
        };
        log.info("=== END TEST: {} [{}] ===", m.getName(), outcome);
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        RestAssured.reset();
    }
}
