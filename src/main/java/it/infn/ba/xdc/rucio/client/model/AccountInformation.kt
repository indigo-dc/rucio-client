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

data class AccountInformation(
        val status: String,
        val account: String,
        val accountType: String,
        val createdAt: String? = null,
        val suspendedAt: String? = null,
        val updatedAt: String? = null,
        val deletedAt: String? = null,
        val email: String? = null
)