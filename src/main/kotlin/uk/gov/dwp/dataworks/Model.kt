package uk.gov.dwp.dataworks

import com.auth0.jwt.interfaces.DecodedJWT
import com.fasterxml.jackson.annotation.JsonCreator
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType
import kotlin.reflect.full.declaredMemberProperties

data class DeployRequest @JsonCreator constructor(
        val jupyterCpu: Int = 512,
        val jupyterMemory: Int = 512,
        val additionalPermissions: List<String> = emptyList()
)

data class JWTObject(val verifiedJWT: DecodedJWT, val userName: String)

data class UserTask(val correlationId: String,
                    val userName: String,
                    val targetGroupArn: String,
                    val albRoutingRuleArn: String,
                    val ecsClusterName: String,
                    val ecsServiceName: String,
                    val iamRoleArn: String,
                    val iamPolicyArn: String) {
    companion object {
        fun from(map: Map<String, String>) = object {
            val correlationId: String by map
            val userName: String by map
            val targetGroupArn: String by map
            val albRoutingRuleArn: String by map
            val ecsClusterName: String by map
            val ecsServiceName: String by map
            val iamRoleArn: String by map
            val iamPolicyArn: String by map
            val data = UserTask(correlationId, userName, targetGroupArn, albRoutingRuleArn, ecsClusterName,
                    ecsServiceName, iamRoleArn, iamPolicyArn)
        }.data

        fun attributes(): List<AttributeDefinition> {
            return UserTask::class.declaredMemberProperties
                    .map { AttributeDefinition.builder().attributeName(it.name).attributeType(ScalarAttributeType.S).build() }
        }
    }

    fun toMap(): Map<String, String> {
        return mapOf(
                "correlationId" to correlationId,
                "userName" to userName,
                "targetGroupArn" to targetGroupArn,
                "albRoutingRuleArn" to albRoutingRuleArn,
                "ecsClusterName" to ecsClusterName,
                "ecsServiceName" to ecsServiceName,
                "iamRoleArn" to iamRoleArn,
                "iamPolicyArn" to iamPolicyArn)
    }
}