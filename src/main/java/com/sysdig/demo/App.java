package com.sysdig.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Demo app using Log4j 2.14.1 — intentionally vulnerable to CVE-2021-44228 (Log4Shell).
 * For Sysdig CNAPP scanning and remediation testing only.
 */
public class App {
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) throws InterruptedException {
        logger.info("Starting Sysdig Log4Shell demo service");
        logger.info("Log4j version: 2.14.1 (vulnerable to CVE-2021-44228)");

        // Keep running so the container stays alive for Sysdig to detect
        while (true) {
            logger.info("Service running — waiting for scan");
            Thread.sleep(30_000);
        }
    }
}
