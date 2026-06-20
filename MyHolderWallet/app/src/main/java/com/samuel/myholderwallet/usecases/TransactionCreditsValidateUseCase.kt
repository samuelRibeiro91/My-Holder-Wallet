package com.samuel.myholderwallet.usecases

import com.samuel.myholderwallet.db.entity.TransactionEntity
import com.samuel.myholderwallet.db.entity.WalletEntity
import com.samuel.myholderwallet.repository.TransactionRepository
import com.samuel.myholderwallet.repository.WalletRepository
import com.samuel.myholderwallet.types.MovementTypes
import kotlinx.coroutines.*
import kotlin.math.roundToInt
import kotlin.math.truncate

class TransactionCreditsValidateUseCase(
    private val walletRepository: WalletRepository,
    private val transactionRepository: TransactionRepository,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {


    suspend fun validateTransactionInsert(transactionEntity: TransactionEntity) {
        val wallet  = returnWalletEntity(transactionEntity.fk_broker!!)

        if (wallet == null){
            throw Exception("Carteira não cadastrada!")
        }
        else
        {
            when (transactionEntity.type){
                MovementTypes.CASH_WITHDRAWAL ,
                MovementTypes.BUY_PAPERS -> {
                    val credits = wallet.credit
                    val oldUsedCredits = transactionEntity.credit

                    val buyValue = (transactionEntity.value * transactionEntity.quantity) - transactionEntity.cost

                    var finalCredit = 0.0f
                    var usedCredit = 0.0f


                    if (credits+oldUsedCredits >= buyValue){
                        finalCredit = (credits+oldUsedCredits) - buyValue
                        usedCredit = buyValue
                    }

                    if (credits+oldUsedCredits < buyValue){
                        finalCredit = 0.0f
                        usedCredit = credits+oldUsedCredits
                    }

                    transactionEntity.credit = usedCredit
                    wallet.credit = finalCredit

                    walletRepository.update(wallet)
                }

                MovementTypes.INFLOW_DIVIDENDS ->{
                    val finalCredit = wallet.credit + transactionEntity.value

                    transactionEntity.credit = 0.0f

                    wallet.credit = finalCredit
                    walletRepository.update(wallet)
                }

                MovementTypes.STOCK_SPLIT ->{
                    transactionEntity.credit = 0.0f
                    transactionEntity.value  = 0.0f
                    val vQuantity = transactionRepository.getQuantitiesOfPaperByBroker(transactionEntity.fk_broker!!, transactionEntity.fk_paper!!)

                    if (vQuantity <= 0.0) throw  Exception("Não há compras desse papel nessa corretora!")


                    var vTotal = (vQuantity * (transactionEntity.factor-1))
                    transactionEntity.quantity = vTotal.toInt()
                }

                MovementTypes.STOCK_BONUS -> {
                    transactionEntity.credit = 0.0f
                    transactionEntity.value  = 0.0f

                    val vQuantity = transactionRepository.getQuantitiesOfPaperByBroker(transactionEntity.fk_broker!!, transactionEntity.fk_paper!!)

                    if (vQuantity <= 0.0) throw  Exception("Não há compras desse papel nessa corretora!")

                    var vTotal: Int = (truncate(vQuantity / transactionEntity.factor).toInt())
                    transactionEntity.quantity = vTotal
                }

                MovementTypes.STOCK_INPLIT -> {
                    transactionEntity.credit = 0.0f

                    val vQuantity = transactionRepository.getQuantitiesOfPaperByBroker(transactionEntity.fk_broker!!, transactionEntity.fk_paper!!)

                    if (vQuantity <= 0.0) throw  Exception("Não há compras desse papel nessa corretora!")

                    val vTransCorrection = TransactionEntity(
                      quantity = vQuantity.toInt() * -1,
                      value =  (transactionEntity.value / vQuantity),
                      type =   MovementTypes.STOCK_INPLIT,
                      fk_paper = transactionEntity.fk_paper,
                      fk_broker = transactionEntity.fk_broker,
                      date = transactionEntity.date
                    )

                    transactionRepository.insert(vTransCorrection)


                    var vTotal: Int = (truncate(vQuantity / transactionEntity.factor).toInt())

                    transactionEntity.quantity = vTotal
                }

                MovementTypes.MONEY_DEPOSIT,
                MovementTypes.SELL_PAPERS,
                MovementTypes.PICKING,
                null -> Unit

            }
        }
    }


    suspend fun validateTransactionDelete(transactionEntity: TransactionEntity) {
        val wallet  = returnWalletEntity(transactionEntity.fk_broker!!)

        if (wallet == null){
            throw Exception("Carteira não cadastrada!")
        }
        else
        {
            when (transactionEntity.type){
                MovementTypes.CASH_WITHDRAWAL,
                MovementTypes.BUY_PAPERS -> {
                    val credits = wallet.credit

                    val usedCredit = transactionEntity.credit

                    val finalCredit = credits + usedCredit

                    wallet.credit = finalCredit

                    walletRepository.update(wallet)
                }

                MovementTypes.INFLOW_DIVIDENDS ->{
                    val finalCredit = wallet.credit - transactionEntity.value

                    wallet.credit = finalCredit
                    walletRepository.update(wallet)
                }

                MovementTypes.STOCK_SPLIT -> {
                    throw Exception("Não é possível excluir esse registro")
                }

                MovementTypes.MONEY_DEPOSIT,
                MovementTypes.SELL_PAPERS,
                MovementTypes.STOCK_INPLIT,
                MovementTypes.STOCK_BONUS,
                MovementTypes.PICKING,
                null -> Unit
            }
        }
    }

    suspend fun validateTransactionUpdate(transactionEntity: TransactionEntity, oldValue: Float)  {
        val wallet  = returnWalletEntity(transactionEntity.fk_broker!!)

        if (wallet == null){
           throw Exception("Carteira não cadastrada!")
        }
        else
        {
            when (transactionEntity.type){
                MovementTypes.CASH_WITHDRAWAL,
                MovementTypes.BUY_PAPERS -> {
                    val credits = wallet.credit
                    val oldUsedCredits = transactionEntity.credit

                    val buyValue = (transactionEntity.value * transactionEntity.quantity) - transactionEntity.cost

                    var finalCredit = 0.0f
                    var usedCredit = 0.0f


                    if (credits+oldUsedCredits >= buyValue){
                        finalCredit = (credits+oldUsedCredits) - buyValue
                        usedCredit = buyValue
                    }

                    if (credits+oldUsedCredits < buyValue){
                        finalCredit = 0.0f
                        usedCredit = credits+oldUsedCredits
                    }

                    transactionEntity.credit = usedCredit
                    wallet.credit = finalCredit

                    walletRepository.update(wallet)
                }

                MovementTypes.INFLOW_DIVIDENDS ->{
                    val finalCredit = wallet.credit - oldValue + transactionEntity.value

                    transactionEntity.credit = 0.0f

                    wallet.credit = finalCredit
                    walletRepository.update(wallet)
                }
                MovementTypes.STOCK_SPLIT -> {
                    throw Exception("Não é possível atualizar esse registro")
                }

                MovementTypes.MONEY_DEPOSIT,
                MovementTypes.SELL_PAPERS,
                MovementTypes.STOCK_INPLIT,
                MovementTypes.STOCK_BONUS,
                MovementTypes.PICKING,
                null -> Unit
            }
        }
    }

    private suspend fun returnWalletEntity(fkbroker: Long): WalletEntity? = withContext(coroutineDispatcher) {
        var wallet : WalletEntity? = walletRepository.getByBroker(fkbroker)

        if (wallet == null){
            val value = walletRepository.insert(WalletEntity(broker = fkbroker))

            if (value > 0)
                wallet = walletRepository.get(value)
        }

        wallet
    }
}
