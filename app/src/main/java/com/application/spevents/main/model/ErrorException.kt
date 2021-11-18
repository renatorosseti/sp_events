package com.application.spevents.main.model

import java.io.IOException

open class NetworkException(error: Throwable): IOException(error)

class NoNetworkException(error: Throwable): NetworkException(error)

class ServerUnreachableException(error: Throwable): NetworkException(error)

class HttpCallFailureException(error: Throwable): NetworkException(error)
