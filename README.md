# Welcome Students of 4156

Please follow the assignment specifications on Courseworks when completing this project.

# Bug Fixes
The **static bug finder** I will be using is **PMD**. \
The two rulesets I used are `errorprone.xml` and `maven-pmd-plugin-default.xml`.
I manually added the PMD plugin into my `pom.xml` file which can be found on **line 34**.

The commands I used to run PMD are:
`mvn pmd:pmd` and `mvn pmd:check`

To analyze the results I looked at the `pmd.xml` file generated located in
`target` directory.