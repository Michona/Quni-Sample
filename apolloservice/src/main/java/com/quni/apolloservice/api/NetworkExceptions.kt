package com.quni.apolloservice.api

/** Added as a cause when there are "business" errors from the response of the endpoint
 * (e.g. wrong password or username) */
open class BusinessException(message: String?) : Exception(message)

/** When the session ID expires if the user is authenticated. */
open class ExpiredSidException() : Exception()
