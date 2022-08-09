package com.samuel.myholderwallet.usecases

import com.samuel.myholderwallet.db.entity.TransactionEntity
import com.samuel.myholderwallet.db.entity.WalletEntity
import com.samuel.myholderwallet.repository.WalletRepository
import com.samuel.myholderwallet.types.MovementTypes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class TransactionCreditsValidateUseCase(
    private val walletRepository: WalletRepository
) {


    fun validateTransactionInsert(transactionEntity: TransactionEntity) = MainScope().launch {
        var wallet : WalletEntity? = walletRepository.getByBroker(transactionEntity.fk_broker!!)

        if (wallet == null){
            val value = walletRepository.insert(WalletEntity(broker = transactionEntity.fk_broker!!))

            if (value > 0)
                wallet = walletRepository.get(value)
        }

        if (wallet == null){
            //erro
        }
        else
        {
            when (transactionEntity.type){
                MovementTypes.SELL_PAPERS -> {

                }
                MovementTypes.CASH_WITHDRAWAL ->{
                    var credits = wallet!!.credit
                    val buyValue = (transactionEntity.value * transactionEntity.quantity) - transactionEntity.cost

                    var finalCredit: Float = 0.0f
                    var usedCredit: Float = 0.0f


                    if (credits >= buyValue){
                        finalCredit = credits - buyValue
                        usedCredit = buyValue
                    }

                    if (credits < buyValue){
                        finalCredit = 0.0f
                        usedCredit = credits
                    }

                    transactionEntity.credit = usedCredit
                    wallet!!.credit = finalCredit

                    walletRepository.update(wallet)
                }
                MovementTypes.MONEY_DEPOSIT ->{

                }
                MovementTypes.BUY_PAPERS -> {

                    var credits = wallet!!.credit
                    val buyValue = (transactionEntity.value * transactionEntity.quantity) - transactionEntity.cost

                    var finalCredit: Float = 0.0f
                    var usedCredit: Float = 0.0f


                    if (credits >= buyValue){
                        finalCredit = credits - buyValue
                        usedCredit = buyValue
                    }

                    if (credits < buyValue){
                        finalCredit = 0.0f
                        usedCredit = credits
                    }

                    transactionEntity.credit = usedCredit
                    wallet!!.credit = finalCredit

                    walletRepository.update(wallet)
                }

                MovementTypes.INFLOW_DIVIDENDS ->{

                }
            }
        }
    }


}