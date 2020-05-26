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

package it.infn.ba.xdc.rucio.client

import feign.Param
import feign.QueryMap
import feign.RequestLine
import it.infn.ba.xdc.rucio.client.model.*

interface Rucio {

    @RequestLine("GET /accounts/{account_id}")
    fun getAccount(@Param("account_id") accountId: String): AccountInformation

    @RequestLine("GET /rules/")
    fun getRules(@QueryMap filters: Map<String, String>): ResponseList<RuleInformation>

    @RequestLine("GET /rules/{rule_id}")
    fun getRule(@Param("rule_id") ruleId: String): RuleInformation

    @RequestLine("POST /rules/")
    fun createRule(newRule: RuleCreation): List<String>

    @RequestLine("DELETE /rules/{rule_id}")
    fun deleteRule(@Param("rule_id") ruleId: String)

    @RequestLine("PUT /rules/{rule_id}")
    fun updateRule(@Param("rule_id") ruleId: String, ruleUpdate: RuleUpdate)

    @RequestLine("GET /replicas/{scope}/{name}")
    fun getReplica(@Param("scope") scope: String, @Param("name") name: String): ReplicaInformation
}
