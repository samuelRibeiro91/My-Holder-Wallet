package com.samuel.myholderwallet.usecases

import com.samuel.myholderwallet.db.entity.TransactionEntity
import com.samuel.myholderwallet.db.entity.WalletEntity
import com.samuel.myholderwallet.repository.WalletRepository
import com.samuel.myholderwallet.types.MovementTypes
import kotlinx.coroutines.*

class TransactionCreditsValidateUseCase(
    private val walletRepository: WalletRepository,
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
