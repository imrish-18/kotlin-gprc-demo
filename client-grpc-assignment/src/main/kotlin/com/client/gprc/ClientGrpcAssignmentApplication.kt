package com.client.gprc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class 	ClientGrpcAssignmentApplication

fun main(args: Array<String>) {
	runApplication<ClientGrpcAssignmentApplication>(*args)
	println("hello this is running grpc application.....")
}
