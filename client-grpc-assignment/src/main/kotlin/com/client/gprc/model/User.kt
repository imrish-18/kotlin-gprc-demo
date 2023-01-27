package com.client.gprc.model

import org.springframework.data.annotation.Id
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.lang.NonNull

@Document("users")
data class User(@Id val userId:Int,
                @field:NonNull val firstName:String,
                @field:NotBlank val lastName:String,
                @field:NotBlank val userName:String,
                @field:Size( min = 8, max=12, message = "please enter password length must be between 8 and 12") @NotBlank @NonNull
                val password:String,
                @field:Size(min = 10 ,max = 10,message = "please enter 10 digit valid number")
                val contact:String,
                @field:Email @get:Pattern(regexp = "([a-z])+@([a-z])+\\.com", message = "Email should match the pattern a-z @ a-z .com")
                val email: String

)