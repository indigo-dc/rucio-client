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

import feign.Response
import feign.codec.ErrorDecoder
import it.infn.ba.xdc.rucio.client.common.ModelUtils
import it.infn.ba.xdc.rucio.client.exceptions.*
import it.infn.ba.xdc.rucio.client.model.ErrorResponse
import java.io.IOException

class RucioErrorDecoder : ErrorDecoder {
    override fun decode(methodKey: String, response: Response): RucioException {
        val statusCode = response.status()
        try {
            val errorResponse = ModelUtils.JACKSON_DECODER.decode(response, ErrorResponse::class.java) as ErrorResponse?
            if (errorResponse != null) {
                val exceptionClass = errorResponse.exceptionClass
                val errorMessage = errorResponse.exceptionMessage
                return when (exceptionClass) {
                    DuplicateRuleException.EXCEPTION_CLASS -> DuplicateRuleException(statusCode, errorMessage)
                    UnauthorizedException.EXCEPTION_CLASS -> UnauthorizedException(statusCode, errorMessage)
                    else -> RucioApiException(statusCode, exceptionClass, errorMessage)
                }
            }
        } catch (ex: IOException) {
            // do nothing
        } catch (ex: RuntimeException) {
            // do nothing
        }
        val errorMessage = response.body()?.toString()
        return when (statusCode) {
            404 -> NotFoundException(errorMessage)
            else -> RucioHttpException(statusCode, errorMessage)
        }
    }
}