package com.samuel.myholderwallet.usecases

import android.content.Context
import com.samuel.myholderwallet.db.entity.TransactionEntity
import com.samuel.myholderwallet.db.entity.WalletEntity
import com.samuel.myholderwallet.repository.WalletRepository
import com.samuel.myholderwallet.types.MovementTypes
import kotlinx.coroutines.*

class TransactionCreditsValidateUseCase(
    private val walletRepository: WalletRepository,
    private val context: Context
) {


    fun validateTransactionInsert(transactionEntity: TransactionEntity) = MainScope().launch {
        var wallet  = returnWalletEntity(transactionEntity.fk_broker!!)

        if (wallet == null){
            //erro
        }
        else
        {
            when (transactionEntity.type){
                MovementTypes.CASH_WITHDRAWAL ->{
                    val credits = wallet!!.credit
                    val oldUsedCredits = transactionEntity.credit

                    val buyValue = (transactionEntity.value * transactionEntity.quantity) - transactionEntity.cost

                    var finalCredit: Float = 0.0f
                    var usedCredit: Float = 0.0f


                    if (credits+oldUsedCredits >= buyValue){
                        finalCredit = (credits+oldUsedCredits) - buyValue
                        usedCredit = buyValue
                    }

                    if (credits+oldUsedCredits < buyValue){
                        finalCredit = 0.0f
                        usedCredit = credits+oldUsedCredits
                    }

                    transactionEntity.credit = usedCredit
                    wallet!!.credit = finalCredit

                    walletRepository.update(wallet)
                }
                MovementTypes.BUY_PAPERS -> {
                    val credits = wallet!!.credit
                    val oldUsedCredits = transactionEntity.credit

                    val buyValue = (transactionEntity.value * transactionEntity.quantity) - transactionEntity.cost

                    var finalCredit: Float = 0.0f
                    var usedCredit: Float = 0.0f


                    if (credits+oldUsedCredits >= buyValue){
                        finalCredit = (credits+oldUsedCredits) - buyValue
                        usedCredit = buyValue
                    }

                    if (credits+oldUsedCredits < buyValue){
                        finalCredit = 0.0f
                        usedCredit = credits+oldUsedCredits
                    }

                    transactionEntity.credit = usedCredit
                    wallet!!.credit = finalCredit

                    walletRepository.update(wallet)
                }

                MovementTypes.INFLOW_DIVIDENDS ->{
                    val finalCredit = wallet!!.credit + transactionEntity.value

                    transactionEntity.credit = 0.0f

                    wallet!!.credit = finalCredit
                    walletRepository.update(wallet)
                }
            }
        }
    }


    fun validateTransactionDelete(transactionEntity: TransactionEntity) = MainScope().launch {
        var wallet  = returnWalletEntity(transactionEntity.fk_broker!!)

        if (wallet == null){
                //erro
        }
        else
        {
            when (transactionEntity.type){
                MovementTypes.CASH_WITHDRAWAL ->{
                    var credits = wallet!!.credit

                    var usedCredit = transactionEntity.credit

                    var finalCredit = credits + usedCredit

                    wallet!!.credit = finalCredit

                    walletRepository.update(wallet)
                }
                MovementTypes.BUY_PAPERS -> {
                    var credits = wallet!!.credit

                    var usedCredit = transactionEntity.credit

                    var finalCredit = credits + usedCredit

                    wallet!!.credit = finalCredit

                    walletRepository.update(wallet)
                }

                MovementTypes.INFLOW_DIVIDENDS ->{
                    val finalCredit = wallet!!.credit - transactionEntity.value

                    wallet!!.credit = finalCredit
                    walletRepository.update(wallet)
                }
            }
        }
    }

    fun validateTransactionUpdate(transactionEntity: TransactionEntity, oldValue: Float) = MainScope().launch {
        var wallet  = returnWalletEntity(transactionEntity.fk_broker!!)

        if (wallet == null){
            //erro
        }
        else
        {
            when (transactionEntity.type){
                MovementTypes.CASH_WITHDRAWAL ->{
                    var credits = wallet!!.credit

                    var usedCredit = transactionEntity.credit

                    var finalCredit = credits + usedCredit

                    wallet!!.credit = finalCredit

                    walletRepository.update(wallet)
                }
                MovementTypes.BUY_PAPERS -> {
                    var credits = wallet!!.credit

                    var usedCredit = transactionEntity.credit

                    var finalCredit = credits + usedCredit

                    wallet!!.credit = finalCredit

                    walletRepository.update(wallet)
                }

                MovementTypes.INFLOW_DIVIDENDS ->{
                    val finalCredit = wallet!!.credit - oldValue + transactionEntity.value

                    wallet!!.credit = finalCredit
                    walletRepository.update(wallet)
                }
            }
        }
    }

    private suspend fun returnWalletEntity(fk_broker: Long): WalletEntity? = withContext(Dispatchers.IO) {
        var wallet : WalletEntity? = walletRepository.getByBroker(fk_broker!!)

        if (wallet == null){
            val value = walletRepository.insert(WalletEntity(broker = fk_broker!!))

            if (value > 0)
                wallet = walletRepository.get(value)
        }

        wallet
    }
}
