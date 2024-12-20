val mockitoAgent = configurations.create("mockitoAgent")

var springdocVersion = "2.3.0"
var javaxPersistenceVersion = "2.2"
val log4jVersion = "2.24.3"
val mariadbJavaClientVersion = "2.7.4"
val mockitoVersion = "5.14.2"

plugins {
    java
    id("org.springframework.boot") version "3.4.0"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "com.lescours"
version = "0.0.6-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(23)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Spring starters
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-log4j2")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa") {
        exclude(group = "ch.qos.logback", module = "logback-classic")
        exclude(group = "ch.qos.logback", module = "logback-core")
        exclude(group = "org.apache.logging.log4j", module="log4j-to-slf4j")
    }
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:$springdocVersion")

    // Logging
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")

    // Database
    runtimeOnly("org.mariadb.jdbc:mariadb-java-client:$mariadbJavaClientVersion")
    implementation("javax.persistence:javax.persistence-api:$javaxPersistenceVersion")
    implementation("org.hibernate.orm:hibernate-community-dialects")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Testing
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testRuntimeOnly("com.h2database:h2")
    testImplementation("org.mockito:mockito-core:$mockitoVersion")
    mockitoAgent("org.mockito:mockito-core:$mockitoVersion") { isTransitive = false }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    compileClasspath {
        exclude(group = "ch.qos.logback", module = "logback-classic")
        exclude(group = "ch.qos.logback", module = "logback-core")
        exclude(group = "org.apache.logging.log4j", module="log4j-to-slf4j")
    }
    runtimeClasspath {
        // On exclue logback et log4j-to-slf4j pour éviter les conflits de logging
        // vu qu'on va utiliser log4j2 et log4j-slf4j-impl
        exclude(group = "ch.qos.logback", module = "logback-classic")
        exclude(group = "ch.qos.logback", module = "logback-core")
        exclude(group = "org.apache.logging.log4j", module="log4j-to-slf4j")
    }
    testCompileClasspath {
        exclude(group = "ch.qos.logback", module = "logback-classic")
        exclude(group = "ch.qos.logback", module = "logback-core")
        exclude(group = "org.apache.logging.log4j", module="log4j-to-slf4j")
        exclude(group = "org.mariadb.jdbc", module="mariadb-java-client")
    }
    testRuntimeClasspath {
        // Pareil que pour plus haut mais on exclue aussi mariadb-java-client (on utilise h2)
        exclude(group = "ch.qos.logback", module = "logback-classic")
        exclude(group = "ch.qos.logback", module = "logback-core")
        exclude(group = "org.apache.logging.log4j", module="log4j-to-slf4j")
        exclude(group = "org.mariadb.jdbc", module="mariadb-java-client")
    }
}

tasks.processResources {
    filesMatching("**/application.properties") {
        expand("projectVersion" to project.version)
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    include("**/*Tests*")
    doFirst {
        jvmArgs = listOf(
            "-javaagent:${mockitoAgent.singleFile}",
            "-Dspring.profiles.active=test"
        )
    }
}
