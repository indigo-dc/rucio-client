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

import feign.Client
import feign.Feign
import feign.Logger
import feign.RequestInterceptor
import feign.okhttp.OkHttpClient
import feign.slf4j.Slf4jLogger
import it.infn.ba.xdc.rucio.client.auth.OidcTokenRequestInterceptor
import it.infn.ba.xdc.rucio.client.common.ModelUtils
import java.security.cert.X509Certificate
import javax.net.ssl.*


class RucioClient {
    companion object {
        @JvmStatic
        fun getInstance(endpoint: String, vararg interceptors: RequestInterceptor): Rucio {
            val b = Feign.builder()
                    .client(getTrustEveryoneClient())
                    .encoder(ModelUtils.JACKSON_ENCODER)
                    .decoder(ModelUtils.JACKSON_DECODER)
                    .errorDecoder(RucioErrorDecoder())
                    .requestInterceptors(listOf(*interceptors))
                    .logger(Slf4jLogger())
                    .logLevel(Logger.Level.BASIC)

            return b.target(Rucio::class.java, endpoint)
        }

        @JvmStatic
        fun getInstanceWithOidcAuth(endpoint: String, oidcToken: String): Rucio {
            return getInstance(endpoint, OidcTokenRequestInterceptor(oidcToken))
        }

        private fun getTrustEveryoneClient(): Client {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                }

                override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                }

                override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
            })
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, java.security.SecureRandom())
            val sslSocketFactory = sslContext.socketFactory
            val okHttpClient = okhttp3.OkHttpClient.Builder()
                    .followRedirects(true)
                    .sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager)
                    .hostnameVerifier { _, _ -> true }
                    .build()
            return OkHttpClient(okHttpClient)
        }
    }
}

class NoOpHostnameVerifier : HostnameVerifier {
    override fun verify(hostname: String?, session: SSLSession?): Boolean {
        return true
    }
}

class NoOpX509TrustManager : X509TrustManager {
    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
    }

    override fun getAcceptedIssuers(): Array<X509Certificate>? {
        return null
    }

}
