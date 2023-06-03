package com.uptech.buanderie.exceptions

import org.springframework.security.core.AuthenticationException

class ArchivedUserException(message: String) : AuthenticationException(message)