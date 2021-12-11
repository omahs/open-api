package io.openfuture.api.config.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.openfuture.api.domain.exception.ExceptionResponse
import io.openfuture.api.util.getIpRange
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.util.matcher.IpAddressMatcher
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ApiAuthorizationFilter(private val mapper: ObjectMapper): Filter {

    private val IPV4_LOOPBACK = "127.0.0.1"
    private val IPV6_LOOPBACK = "0:0:0:0:0:0:0:1"
    private var ipList = arrayListOf<String>()
    var allowLocalhost = true

    override fun init(filterConfig: FilterConfig?) {
        ipList = getIpRange("192.168.1.0/28")
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request as HttpServletRequest
        response as HttpServletResponse

        ipList.stream().map { ip -> print(ip) }

       /* if (!isAllowed(request)) {
            deny(response)
            return;
        }*/

        if (!isAllowed(request) && request.requestURI.startsWith("/api") && null == SecurityContextHolder.getContext().authentication) {
            deny(response)
            return
        }

        chain.doFilter(request, response)
    }

    override fun destroy() {
        // Do nothing
    }

    @Throws(IOException::class)
    fun deny(res: HttpServletResponse) {
        val exceptionResponse = ExceptionResponse(UNAUTHORIZED.value(), "Open token is invalid or disabled")
        res.status = exceptionResponse.status
        res.writer.write(mapper.writeValueAsString(exceptionResponse))
    }

    fun isAllowed(request: HttpServletRequest): Boolean {

        val ip = request.remoteAddr
        if (allowLocalhost && (IPV4_LOOPBACK == ip || IPV6_LOOPBACK == ip)) {
            return true
        }

        val matcher = IpAddressMatcher("192.168.1.0/24")

        if (!matcher.matches(request.getHeader("X-Forwarded-For"))) {
            return true
        }

        return false
    }

}