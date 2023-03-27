plugins {
  java
  `maven-publish`
  kotlin("jvm") version io.ol.core.constant.OLCoreVersions.KOTLIN_VERSION
  id("org.springframework.boot") version io.ol.core.constant.OLCoreVersions.SPRING_BOOT_VERSION
}

val projectVersion: String by project
group = "io.openlegacy"
version = projectVersion

repositories {
  maven {
    url = uri("${System.getProperty("user.home")}/.m2/repository")
  }
  val olUsername = "public"
  val olPassword = "AP3UUPpoUv5HMRpNWwEXHKremER"
  mavenLocal()
  mavenCentral()
  maven {
    name = "openlegacy-m2-public"
    url = uri("https://openlegacy.jfrog.io/openlegacy/ol-public")
    credentials {
      username = olUsername
      password = olPassword
    }
  }
  maven {
    name = "ol-3rd-party-libs"
    url = uri("https://openlegacy.jfrog.io/openlegacy/ol-3rd-party-libs")
    credentials {
      username = olUsername
      password = olPassword
    }
  }
}

dependencies {
  val openlegacyVersion: String by project
  val loki4jAppenderVersion: String by project
  val pushGatewayClientVersion: String by project
  implementation(enforcedPlatform("io.openlegacy:openlegacy-spring-bom:$openlegacyVersion"))
  implementation("io.openlegacy.springboot:flow-spring-webflux-autoconfiguration:$openlegacyVersion")
  implementation("io.openlegacy.springboot:mf-cics-ts-autoconfiguration:$openlegacyVersion")
  implementation("io.openlegacy.springboot:properties-encryption-autoconfiguration:$openlegacyVersion")
  implementation("com.github.loki4j:loki-logback-appender:$loki4jAppenderVersion")
  implementation("com.ibm.icu:icu4j:71.1")
  implementation("io.micrometer:micrometer-registry-prometheus")
  implementation("io.prometheus:simpleclient_pushgateway:$pushGatewayClientVersion")
  implementation("org.springframework.boot:spring-boot-starter-actuator")
}

tasks {
  val javaVersion: String by project
  withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
      jvmTarget = javaVersion
    }
  }

  withType<JavaCompile> {
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
    options.encoding = "UTF-8"
  }

  named<Test>("test") {
    useJUnitPlatform()
  }
}

java {
  withSourcesJar()
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      versionMapping {
        usage("java-api") {
          fromResolutionOf("runtimeClasspath")
        }
        usage("java-runtime") {
          fromResolutionResult()
        }
      }
    }
  }
}
