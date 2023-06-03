package com.uptech.buanderie.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import com.uptech.buanderie.Log
import com.uptech.buanderie.services.implementations.UserDetailsServiceImpl
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


// valider token request
class AuthenticationJwtTokenFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var userDetailsService: UserDetailsServiceImpl

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val jwt = SecurityUtils.parseJwt(request)
            if (jwt != null && SecurityUtils.validateJwtToken(jwt)) {
                val username = SecurityUtils.getUserNameFromJwtToken(jwt)
                var issuer = SecurityUtils.getUserIssuerFromJwtToken(jwt)
                val userDetails = userDetailsService.loadUserByUsername(username)

               if (isAuthoritiesChanged(issuer, userDetails.authorities.toList())) {
                    throw Exception("authorities not valid")
                }
                val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (e: Exception) {
            Log.e("Cannot set user authentication: {}", e.message ?: "error Msg")
        }
        filterChain.doFilter(request, response)
    }


    private fun isAuthoritiesChanged(issuer:String,newAuthorityList : List<GrantedAuthority>):Boolean{



        val oldAuthorityStr = issuer.replace("[","").replace("]","").replace(" ","")

        var oldAuthorityList = mutableListOf<String>()
        var resultList = mutableListOf<String>()

        if (oldAuthorityStr.isNotEmpty()){
            oldAuthorityList.clear()
            oldAuthorityList.addAll( oldAuthorityStr.split(","))
        }

        if (oldAuthorityList.size != newAuthorityList.size){
            return true
        } else {
            newAuthorityList.forEach {
                if (oldAuthorityList.contains(it.authority.trim())){
                    resultList.add(it.authority)
                }
            }
        }
        println("resultList::::${resultList.size}")

        return resultList.size != newAuthorityList.size
    }

}