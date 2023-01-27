import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.ofSourceSet
import com.google.protobuf.gradle.plugins
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc

object Ver {
	const val grpcSpringBootStarter = "3.5.6"
	const val protobuf = "3.12.3" // use compatible version with grpc-spring-boot-starter
	const val grpc = "1.31.0" // use compatible version with grpc-spring-boot-starter
	const val reactorGrpc = "1.0.1"
}
plugins {
	id("org.springframework.boot") version "3.0.1"
	id("io.spring.dependency-management") version "1.1.0"
	id("com.google.protobuf") version "0.8.12" //protobuf gradle plugin
	idea //IDE 플러그인 넣어줘야 generated 소스들이 인지됨.
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
}

group = "com.-client-grpc"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
	implementation("io.openliberty.features:beanValidation-3.0:22.0.0.7")
	testImplementation("org.springframework.batch:spring-batch-test")
	// https://mvnrepository.com/artifact/jakarta.annotation/jakarta.annotation-api
	implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
	// https://mvnrepository.com/artifact/jakarta.validation/jakarta.validation-api
	implementation("jakarta.validation:jakarta.validation-api:3.0.2")
	// https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-validation
	implementation("org.springframework.boot:spring-boot-starter-validation:3.0.1")
// https://mvnrepository.com/artifact/javax.validation/validation-api
	implementation("javax.validation:validation-api:2.0.1.Final")
	implementation("org.springframework.boot:spring-boot-starter-logging")
	// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-sleuth
	implementation("org.springframework.cloud:spring-cloud-sleuth:3.1.5")
// https://mvnrepository.com/artifact/org.springframework.cloud/spring-cloud-sleuth-zipkin
	implementation("org.springframework.cloud:spring-cloud-sleuth-zipkin:3.1.5")

	//grpc
	implementation("com.salesforce.servicelibs:reactor-grpc-stub:1.2.3")
	implementation("io.grpc:grpc-protobuf:1.51.0")
	implementation("io.grpc:grpc-services:1.51.0")
	implementation("io.github.lognet:grpc-spring-boot-starter:5.0.0") //Spring Boot starter module for gRPC framework

	//runtimeOnly("io.grpc:grpc-netty") //없으면 grpc 포트가 열리지 않음, default 6565
	//for test
	testImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
	}
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:${Ver.protobuf}"
	}
	plugins {
		id("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:${Ver.grpc}"
		}
		id("reactor") {
			artifact = "com.salesforce.servicelibs:reactor-grpc:${Ver.reactorGrpc}"
		}
	}
	generateProtoTasks {
		ofSourceSet("main").forEach {
			it.plugins {
				// Apply the "grpc" and "reactor" plugins whose specs are defined above, without options.
				id("grpc")
				id("reactor")
			}
		}
	}
}