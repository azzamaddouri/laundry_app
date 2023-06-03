package com.uptech.buanderie.exception

import org.springframework.security.core.AuthenticationException

class DisabledUserException(message: String) : AuthenticationException(message)