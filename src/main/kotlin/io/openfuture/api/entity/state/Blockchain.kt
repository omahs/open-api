package io.openfuture.api.entity.state

import com.fasterxml.jackson.annotation.JsonValue

enum class Blockchain(private val value: String) {

    Ethereum("EthereumBlockchain"),
    Ropsten("RopstenBlockchain"),
    Bitcoin("BitcoinBlockchain"),
    Binance("BinanceBlockchain"),
    BinanceTestnetBlockchain("BinanceTestnetBlockchain");

    companion object {
        fun getBlockchainBySymbol(symbol: String): Blockchain {
            return when (symbol) {
                "ETH" -> Ropsten
                "BNB" -> Binance
                "BTC" -> Bitcoin
                else -> Ropsten
            }
        }
    }

    @JsonValue
    fun getValue(): String = value

}
