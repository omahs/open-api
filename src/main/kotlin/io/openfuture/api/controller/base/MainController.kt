package io.openfuture.api.controller.base

import io.openfuture.api.config.propety.WidgetProperties
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Controller
class MainController(
    private val widgetProperties: WidgetProperties
) {

    @GetMapping("/", "/ethereum-scaffold", "/ethereum-scaffolds", "/ethereum-scaffolds/**", "/applications", "/applications/**", "/keys", "/keys/**", "/scaffolds/**")
    fun frontend() = "frontend"

    @GetMapping("/widget/{address}")
    fun widget(@PathVariable address: String, model: ModelMap): String {
        model["address"] = address
        model["host"] = widgetProperties.host
        return "widget"
    }

    @GetMapping("/widget/transactions/address/{address}")
    fun transactionByAddress(@PathVariable address: String, model: ModelMap): String {
        model["value"] = address
        model["type"] = "address"
        model["host"] = widgetProperties.host
        return "tracker"
    }

    @GetMapping("/widget/transactions/order/{orderKey}")
    fun transactionByOrder(@PathVariable orderKey: String, model: ModelMap): String {
        model["value"] = orderKey
        model["type"] = "order"
        model["host"] = widgetProperties.host
        return "tracker"
    }

}