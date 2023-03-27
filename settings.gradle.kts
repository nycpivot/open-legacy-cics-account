rootProject.apply {
  name = "demo-ol-cics"
}

pluginManagement {
  repositories {
    maven {
      url = uri("${System.getProperty("user.home")}/.m2/repository")
    }
    gradlePluginPortal()
  }
}


