package com.uptech.buanderie.exceptions

import org.springframework.security.core.AuthenticationException

class DisabledUserException(message: String) : AuthenticationException(message)