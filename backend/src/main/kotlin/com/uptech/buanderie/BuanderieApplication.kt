package com.uptech.buanderie

import com.uptech.buanderie.services.IRoleService
import com.uptech.buanderie.services.IUserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class BuanderieApplication : CommandLineRunner, SpringBootServletInitializer() {
    @Autowired
    private lateinit var roleService: IRoleService

    @Autowired
    private lateinit var userService: IUserService

    override fun run(vararg args: String?) {
        roleService.init()
        userService.init()
    }
}

fun main(args: Array<String>) {
    runApplication<BuanderieApplication>(*args)
}


fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder? {
    Log.i("buanderieApplication Start")
    return application.sources(BuanderieApplication::class.java)
    Log.i("buanderieApplication Starting End")
}