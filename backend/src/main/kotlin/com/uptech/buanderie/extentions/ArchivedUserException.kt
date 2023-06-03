package com.uptech.buanderie.exception

import org.springframework.security.core.AuthenticationException

class ArchivedUserException(message: String) : AuthenticationException(message)