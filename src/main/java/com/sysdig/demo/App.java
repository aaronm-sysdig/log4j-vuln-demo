package com.sysdig.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Demo app using Log4j 2.14.1 — intentionally vulnerable to CVE-2021-44228 (Log4Shell).
 * For Sysdig CNAPP scanning and remediation testing only.
 */
public class App {
    private static final Logger logger = LogManager.getLogger(App.class);
    private static final String LOG4J_VERSION = "2.14.1";
    private static final long HEARTBEAT_INTERVAL_MS = 5_000;

    public static void main(String[] args) throws InterruptedException {
        String log4jImplVersion = LoggerContext.class.getPackage().getImplementationVersion();

        logger.info("=== Sysdig Log4Shell Demo Service starting ===");
        logger.info("Log4j version (pom.xml): {}", LOG4J_VERSION);
        logger.info("Log4j version (runtime):  {}", log4jImplVersion != null ? log4jImplVersion : LOG4J_VERSION);
        logger.warn("CVE-2021-44228 (Log4Shell) — CVSS 10.0 — THIS IMAGE IS INTENTIONALLY VULNERABLE");
        logger.info("Heartbeat interval: {}ms", HEARTBEAT_INTERVAL_MS);

        AtomicLong tick = new AtomicLong(0);
        while (true) {
            Thread.sleep(HEARTBEAT_INTERVAL_MS);
            long t = tick.incrementAndGet();
            logger.info("[tick={}] Service alive — log4j-core:{} — uptime {}s", t, LOG4J_VERSION, t * HEARTBEAT_INTERVAL_MS / 1000);
            if (t % 12 == 0) {
                logger.warn("[tick={}] Reminder: running log4j {} — upgrade to 2.17.1+ to fix CVE-2021-44228", t, LOG4J_VERSION);
            }
        }
    }
}
