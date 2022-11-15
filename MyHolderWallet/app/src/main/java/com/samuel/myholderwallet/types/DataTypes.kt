package com.samuel.myholderwallet.types

enum class PaperType(private val Description: String) {
    STOCK("Ação"),
    REIT("FII"),
    ADR("BDR");

    override fun toString() = Description
}

enum class MovementTypes(private val Description: String) {
    MONEY_DEPOSIT("Depósito de dinheiro"),
    BUY_PAPERS("Compra de papéis"),
    INFLOW_DIVIDENDS("Entrada de dividendos"),
    CASH_WITHDRAWAL("Retirada de dinheiro"),
    SELL_PAPERS("Venda de papéis"),
    STOCK_SPLIT("Desdobramento de papéis"),
    STOCK_INPLIT("Grupamento de papéis"),
    STOCK_BONUS("Bonificação de papéis"),
    PICKING("Sobras de desdobramento/grupamento");

    override fun toString() = Description
}