package com.samuel.myholderwallet.types

enum class PaperType(val Description: String) {
    STOCK("Ação"),
    REIT("FII"),
    ADR("BDR");

    override fun toString() = Description
}

enum class MovementTypes(val Description: String) {
    MONEY_DEPOSIT("Depósito de dinheiro"),
    BUY_PAPERS("Compra de papéis"),
    INFLOW_DIVIDENDS("Entrada de dividendos"),
    CASH_WITHDRAWAL("Retirada de dinheiro"),
    SELL_PAPERS("Venda de papéis");

    override fun toString() = Description
}