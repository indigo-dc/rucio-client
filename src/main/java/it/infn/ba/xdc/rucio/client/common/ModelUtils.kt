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

package it.infn.ba.xdc.rucio.client.common

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import feign.Response
import feign.Util
import feign.jackson.JacksonDecoder
import feign.jackson.JacksonEncoder
import it.infn.ba.xdc.rucio.client.model.ResponseList
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


class ModelUtils {
    companion object {

        var mapper: ObjectMapper = ObjectMapper()
                .registerKotlinModule()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .setSerializationInclusion(JsonInclude.Include.NON_EMPTY)

        @JvmField
        val JACKSON_ENCODER = JacksonEncoder(mapper)
        @JvmField
        val JACKSON_DECODER = CustomJacksonDecoder(mapper)
    }

}

class CustomJacksonDecoder(mapper: ObjectMapper) : JacksonDecoder(mapper) {

    private fun isResponseList(type: Type): Boolean {
        return type is ParameterizedType && type.rawType == ResponseList::class.java
    }

    private fun getResponseListParameter(type: Type): Boolean {
        if (type is ParameterizedType) {
            type.actualTypeArguments[0]
        }
        return false
    }

    override fun decode(response: Response, type: Type): Any? {
        return if (response.status() == 404 || response.body() == null) {
            if (isResponseList(type)) {
                ResponseList(listOf<Any>())
            } else {
                Util.emptyValueOf(type)
            }
        } else {
            if (isResponseList(type)) {
                val typeParameter = ModelUtils.mapper.constructType((type as ParameterizedType).actualTypeArguments[0])
                val values = response.body().asReader().useLines { lines -> lines.map { ModelUtils.mapper.readValue<Any>(it, typeParameter) }.toList() }
                ResponseList(values)
            } else {
                super.decode(response, type)
            }
        }
    }
}