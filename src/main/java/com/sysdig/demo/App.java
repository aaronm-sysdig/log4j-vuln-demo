package com.sysdig.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Demo app for Sysdig CNAPP scanning and remediation testing.
 * Detects at runtime whether the bundled log4j version is vulnerable to CVE-2021-44228.
 */
public class App {
    private static final Logger logger = LogManager.getLogger(App.class);
    private static final long HEARTBEAT_INTERVAL_MS = 5_000;

    // Full chain: CVE-2021-44228→2.15.0, CVE-2021-45046→2.16.0, CVE-2021-45105→2.17.0,
    // CVE-2021-44832→2.17.1, CVE-2025-68161→2.25.3, CVE-2026-34477/CVE-2026-34480→2.25.4
    private static final int[] SAFE_VERSION = {2, 25, 4};

    public static void main(String[] args) throws InterruptedException {
        String version = LoggerContext.class.getPackage().getImplementationVersion();
        if (version == null) version = "unknown";

        boolean vulnerable = isVulnerable(version);

        logger.info("=== Sysdig Log4Shell Demo Service starting ===");
        logger.info("Log4j version (runtime): {}", version);

        if (vulnerable) {
            logger.warn("SECURITY STATUS: VULNERABLE — log4j {} is affected by CVE-2021-44228 (Log4Shell, CVSS 10.0)", version);
            logger.warn("Fix: upgrade log4j-core to 2.25.4+ in pom.xml (chain: 2.14.1→2.15.0→2.16.0→2.17.1→2.25.4)");
        } else {
            logger.info("SECURITY STATUS: SAFE — log4j {} is NOT affected by CVE-2021-44228", version);
        }

        logger.info("Heartbeat interval: {}ms", HEARTBEAT_INTERVAL_MS);

        AtomicLong tick = new AtomicLong(0);
        while (true) {
            Thread.sleep(HEARTBEAT_INTERVAL_MS);
            long t = tick.incrementAndGet();

            if (vulnerable) {
                logger.info("[tick={}] log4j-core:{} [VULNERABLE] uptime {}s", t, version, t * HEARTBEAT_INTERVAL_MS / 1000);
                if (t % 12 == 0) {
                    logger.warn("[tick={}] Unpatched CVEs — still running log4j {} — upgrade to 2.25.4+", t, version);
                }
            } else {
                logger.info("[tick={}] log4j-core:{} [SAFE] uptime {}s", t, version, t * HEARTBEAT_INTERVAL_MS / 1000);
            }
        }
    }

    /** Returns true if the given version string is older than SAFE_VERSION. */
    static boolean isVulnerable(String version) {
        try {
            String[] parts = version.split("[.\\-]");
            int[] v = new int[3];
            for (int i = 0; i < 3 && i < parts.length; i++) {
                v[i] = Integer.parseInt(parts[i]);
            }
            for (int i = 0; i < 3; i++) {
                if (v[i] < SAFE_VERSION[i]) return true;
                if (v[i] > SAFE_VERSION[i]) return false;
            }
            return false; // exactly 2.17.1 — safe
        } catch (NumberFormatException e) {
            return true; // can't parse → assume vulnerable
        }
    }
}
