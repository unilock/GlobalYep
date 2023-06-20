plugins {
    java
    alias(libs.plugins.blossom)
    alias(libs.plugins.shadow)
    alias(libs.plugins.runvelocity)
}

repositories {
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://repo.alessiodp.com/releases/") {
        mavenContent {
            includeGroup(libs.libby.get().group)
        }
    }
}

dependencies {
    compileOnly(libs.velocity)
    annotationProcessor(libs.velocity)

    compileOnly(libs.completables)
    compileOnly(libs.configurate)
    implementation(libs.libby)

    compileOnly(libs.miniplaceholders)
}

blossom {
    replaceTokenIn("src/main/java/cc/unilock/globalyep/util/Constants.java")
    replaceToken("{version}", project.version)
    replaceToken("{configurate}", libs.versions.configurate.get())
    replaceToken("{geantyref}", libs.versions.geantyref.get())
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    compileJava {
        options.apply {
            release.set(17)
            encoding = Charsets.UTF_8.name()
        }
    }

    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")
        relocate("org.spongepowered", "cc.unilock.globalyep.libs.spongepowered")
        relocate("net.byteflux", "cc.unilock.globalyep.libs.byteflux")
        relocate("io.leangen.geantyref", "cc.unilock.globalyep.libs.geantyref")
    }

    runVelocity {
        velocityVersion(libs.versions.velocity.get())
    }
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(17))
