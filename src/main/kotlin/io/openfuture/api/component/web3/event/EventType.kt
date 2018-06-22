package io.openfuture.api.component.web3.event

enum class EventType(private val value: String) {

    PAYMENT_COMPLETED("PAYMENT_COMPLETED"),
    FUNDS_DEPOSITED("FUNDS_DEPOSITED"),
    ACTIVATED_SCAFFOLD("ACTIVATED_SCAFFOLD"),
    ADDED_SHARE_HOLDER("ADDED_SHARE_HOLDER"),
    EDITED_SHARE_HOLDER("EDITED_SHARE_HOLDER"),
    DELETED_SHARE_HOLDER("DELETED_SHARE_HOLDER"),
    PAYED_FOR_SHARE_HOLDER("PAYED_FOR_SHARE_HOLDER")
    ;

    fun getValue(): String = value

}