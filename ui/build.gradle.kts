import org.gradle.internal.os.OperatingSystem

plugins {
    base
}

val publicAppDir = file("public")
val privateAppDir = file("private")
val adminAppDir = file("admin")

val staticResourcesDir = file("../src/main/resources/static")

val isWindows = OperatingSystem.current().isWindows
val npm = if (isWindows) "npm.cmd" else "npm"

// Install dependencies for public app
tasks.register<Exec>("installPublicDeps") {
    group = "ui"
    description = "Install npm dependencies for public app"
    workingDir = publicAppDir
    commandLine(npm, "install")
}

// Install dependencies for private app
tasks.register<Exec>("installPrivateDeps") {
    group = "ui"
    description = "Install npm dependencies for private app"
    workingDir = privateAppDir
    commandLine(npm, "install")
}

// Install dependencies for admin app
tasks.register<Exec>("installAdminDeps") {
    group = "ui"
    description = "Install npm dependencies for admin app"
    workingDir = adminAppDir
    commandLine(npm, "install")
}

// Build public app
tasks.register<Exec>("buildPublic") {
    group = "ui"
    description = "Build public Next.js app"
    workingDir = publicAppDir
    commandLine(npm, "run", "build")
    dependsOn("installPublicDeps")
}

// Build private app
tasks.register<Exec>("buildPrivate") {
    group = "ui"
    description = "Build private Next.js app"
    workingDir = privateAppDir
    commandLine(npm, "run", "build")
    dependsOn("installPrivateDeps")
}

// Build admin app
tasks.register<Exec>("buildAdmin") {
    group = "ui"
    description = "Build admin Next.js app"
    workingDir = adminAppDir
    commandLine(npm, "run", "build")
    dependsOn("installAdminDeps")
}

// Copy public app output to static resources
tasks.register<Copy>("copyPublicToStatic") {
    group = "ui"
    description = "Copy public app build output to static resources"
    from(file("$publicAppDir/out"))
    into(file("$staticResourcesDir/public"))
    dependsOn("buildPublic")
}

// Copy private app output to static resources
tasks.register<Copy>("copyPrivateToStatic") {
    group = "ui"
    description = "Copy private app build output to static resources"
    from(file("$privateAppDir/out"))
    into(file("$staticResourcesDir/private"))
    dependsOn("buildPrivate")
}

// Copy admin app output to static resources
tasks.register<Copy>("copyAdminToStatic") {
    group = "ui"
    description = "Copy admin app build output to static resources"
    from(file("$adminAppDir/out"))
    into(file("$staticResourcesDir/admin"))
    dependsOn("buildAdmin")
}

// Build all UIs
tasks.register("buildAllUIs") {
    group = "ui"
    description = "Build all UI apps and copy to static resources"
    dependsOn("copyPublicToStatic", "copyPrivateToStatic", "copyAdminToStatic")
}

// Set the build task to buildAllUIs
tasks.named("build") {
    dependsOn("buildAllUIs")
}

// Clean task to remove node_modules and build outputs
tasks.register<Delete>("cleanUI") {
    group = "ui"
    description = "Clean all UI build artifacts and node_modules"
    delete(
        file("$publicAppDir/node_modules"),
        file("$publicAppDir/.next"),
        file("$publicAppDir/out"),
        file("$privateAppDir/node_modules"),
        file("$privateAppDir/.next"),
        file("$privateAppDir/out"),
        file("$adminAppDir/node_modules"),
        file("$adminAppDir/.next"),
        file("$adminAppDir/out"),
        staticResourcesDir
    )
}

tasks.named("clean") {
    dependsOn("cleanUI")
}
