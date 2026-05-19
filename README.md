# log4j-vuln-demo

**Intentionally vulnerable demo image for Sysdig CNAPP scanning and remediation testing.**

Contains Log4j 2.14.1 — vulnerable to [CVE-2021-44228](https://nvd.nist.gov/vuln/detail/CVE-2021-44228) (Log4Shell, CVSS 10.0 Critical).

## Purpose

This repo exists to demonstrate Sysdig's end-to-end CNAPP workflow:
1. `/sysdig-investigate` — surfaces the vulnerable image and ranks it for remediation
2. `/sysdig-remediate` — resolves a safe fix version (2.15.0+) and opens a PR

## Fix

Bump `log4j-core` and `log4j-api` to **2.17.1** or later in `pom.xml`.

## Image
Use a proper version # so you can then bump it to `1.0.1` or something
```
ghcr.io/aaronm-sysdig/log4j-vuln-demo:1.0.0
```
