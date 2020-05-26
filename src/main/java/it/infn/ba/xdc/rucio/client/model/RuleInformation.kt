/*
 * Copyright © 2019-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package it.infn.ba.xdc.rucio.client.model

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import com.fasterxml.jackson.annotation.JsonProperty

data class RuleInformation(
        val locksOkCnt: Int,
        val sourceReplicaExpression: String?,
        val locksStuckCnt: Int,
        val purgeReplicas: Boolean,
        val rseExpression: String,
        val updatedAt: String?,
        val meta: String?,
        val childRuleId: String?,
        val id: String,
        val ignoreAccountLimit: Boolean,
        val error: String?,
        val weight: String?,
        val locksReplicatingCnt: Int,
        val notification: String,
        val copies: Int,
        val comments: String?,
        val splitContainer: Boolean,
        val priority: Int,
        val state: ReplicationStatus,
        val scope: String,
        val subscriptionId: String?,
        val stuckAt: String?,
        val ignoreAvailability: Boolean,
        val eolAt: String?,
        val expiresAt: String?,
        val didType: String,
        val account: String,
        val locked: Boolean,
        val name: String,
        val createdAt: String,
        val activity: String,
        val grouping: String
)

enum class ReplicationStatus {
    @JsonProperty("OK")
    OK,
    @JsonProperty("STUCK")
    STUCK,
    @JsonProperty("REPLICATING")
    REPLICATING,
    @JsonEnumDefaultValue
    @JsonProperty("UNKNOWN")
    UNKNOWN
}