import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.0"
	id("io.spring.dependency-management") version "1.1.0"
	id("nu.studer.jooq") version "8.1"
    id("org.springdoc.openapi-gradle-plugin") version "1.6.0"
    id("org.flywaydb.flyway") version "9.4.0"
	kotlin("jvm") version "1.8.21"
	kotlin("plugin.spring") version "1.8.21"
}

group = "com.adikmt"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.springframework.boot:spring-boot-starter")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    jooqGenerator("mysql:mysql-connector-java:8.0.28")
    implementation("org.jooq:jooq-codegen")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")

    implementation("mysql:mysql-connector-java:8.0.28")
    implementation("org.flywaydb:flyway-core")


    implementation("org.jooq:jooq-kotlin-coroutines")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

//	implementation ("org.springframework.boot:spring-boot-starter-security")

    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.1.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.springframework.boot:spring-boot-starter-validation")

    developmentOnly("org.springframework.boot:spring-boot-devtools")

    testImplementation("io.projectreactor:reactor-test")
    testImplementation("io.mockk:mockk:1.13.5")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
//	testImplementation ("org.springframework.security:spring-security-test")
}

flyway {
	driver = project.properties["driverClassName"].toString()
	url = project.properties["url"].toString()
	user = project.properties["username"].toString()
	password = project.properties["password"].toString()
}

jooq {
	version.set(project.properties["jooq"].toString())
	edition.set(nu.studer.gradle.jooq.JooqEdition.OSS)

	configurations {
		create("main") {
			// To prevent the unnecessary regeneration of your schema sources!
			generateSchemaSourceOnCompilation.set(true)

			jooqConfiguration.apply {
				jdbc.apply {
					driver = project.properties["driverClassName"].toString()
					url = project.properties["url"].toString()
					user = project.properties["username"].toString()
					password = project.properties["password"].toString()
				}
				generator.apply {
					name = "org.jooq.codegen.KotlinGenerator"
					logging = org.jooq.meta.jaxb.Logging.DEBUG
					database.apply {
						// The database type. The format here is:
						// org.jooq.meta.[database].[database]Database
						name = "org.jooq.meta.mysql.MySQLDatabase"

						// The database schema (or in the absence of schema support, in your RDBMS this
						// can be the owner, user, database name) to be generated. This cannot be combined with the <schemata/> element.
						// If <inputSchema/> is missing then all schemas will be considered.
						inputSchema = "kanbanDB"

						// All elements that are generated from your schema
						// (A Java regular expression. Use the pipe to separate several expressions)
						// Watch out for case-sensitivity. Depending on your database, this might be important!
						// You can create case-insensitive regular expressions using this syntax: (?i:expr).
						// Whitespace is ignored and comments are possible.
						includes = ".*"

						// All elements that are excluded from your schema
						// (A Java regular expression. Use the pipe to separate several expressions).
						// Excludes match before includes, i.e. excludes have a higher priority.
						excludes = """\
						flyway_schema_history | sequences
						| customer_pgs | refresh_top3_product
						| sale_.* | set_.* | get_.* | .*_master
						"""

						// Schema version provider
						schemaVersionProvider = "SELECT MAX(`version`) FROM `flyway_schema_history`"

						// Give enough time to jOOQ to trigger the queries needed for generating sources
						// (default is 5 seconds)
						logSlowQueriesAfterSeconds = 10
					}
					generate.apply {
						isDeprecated = false
						isValidationAnnotations = true
						isSpringAnnotations = true
					}
					target.apply {
						packageName = "jooq.generated"
						directory = "build/kotlin"
					}
				}
			}
		}
	}
}

tasks.named("generateJooq").configure {
	// ensure database schema has been prepared by Flyway before generating the jOOQ sources
//	dependsOn.contains(tasks.named("flywayMigrate"))

	// declare Flyway migration scripts as inputs on the jOOQ task
	inputs.files(fileTree("${rootDir}/../../../../../db/migration/dev/mysql"))
		.withPropertyName("migrations")
		.withPathSensitivity(PathSensitivity.RELATIVE)

	// make jOOQ task participate in incremental builds and build caching
//	allInputsDeclared = true
	outputs.cacheIf { true }
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<ProcessResources>{
	filesMatching("**/application.properties") {
		expand( project.properties )
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
