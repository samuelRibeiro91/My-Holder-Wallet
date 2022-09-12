package com.samuel.myholderwallet.usecases

import com.samuel.myholderwallet.db.entity.TransactionEntity
import com.samuel.myholderwallet.db.entity.WalletEntity
import com.samuel.myholderwallet.repository.WalletRepository
import com.samuel.myholderwallet.types.MovementTypes
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


private val FAKE_WALLETENTITY_NOCREDIT = WalletEntity(id = 1, broker = 1, balance = 0.0f, credit = 0.0f)

private val FAKE_WALLETENTITY_CREDIT   = WalletEntity(id = 1, broker = 1, balance = 0.0f, credit = 200.0f)

@RunWith(MockitoJUnitRunner::class)
class TransactionCreditsValidateUseCaseTest {

    private lateinit var transactionCreditsValidateUseCase : TransactionCreditsValidateUseCase

    @Mock
    private lateinit var walletRepository: WalletRepository

    private var transactionEntity = TransactionEntity(
        id = 1,
        date = 0.0,
        credit = 0.0f,
        type = MovementTypes.BUY_PAPERS,
        cost = 0.0f,
        value = 1.0f,
        quantity = 5,
        fk_paper = 1,
        fk_broker = 1
    )

    private var transactionEntityWithCredits = TransactionEntity(
        id = 1,
        date = 0.0,
        credit = 5.0f,
        type = MovementTypes.BUY_PAPERS,
        cost = 0.0f,
        value = 1.0f,
        quantity = 10,
        fk_paper = 1,
        fk_broker = 1
    )


   @Before
    fun setup(){
        transactionCreditsValidateUseCase = TransactionCreditsValidateUseCase(walletRepository)
    }

    @Test
    fun insert_TransactionEntity_NoCredit() = runTest {
        doReturn(FAKE_WALLETENTITY_NOCREDIT).`when`(walletRepository).getByBroker(1)

        transactionCreditsValidateUseCase.validateTransactionInsert(transactionEntity)

        Assert.assertEquals(0.0f, transactionEntity.credit)
    }

    @Test
    fun insert_TransactionEntity_Credit() = runTest {
        doReturn(FAKE_WALLETENTITY_CREDIT).`when`(walletRepository).getByBroker(1)

        transactionCreditsValidateUseCase.validateTransactionInsert(transactionEntity)

        Assert.assertEquals(5.0f, transactionEntity.credit)
    }

    @Test
    fun update_TransactionEntity_NoCredit() = runTest {
        doReturn(FAKE_WALLETENTITY_CREDIT).`when`(walletRepository).getByBroker(1)


        transactionCreditsValidateUseCase.validateTransactionUpdate(transactionEntity, 0.0f)

        Assert.assertEquals(0.0f, transactionEntity.credit)
    }

    @Test
    fun update_TransactionEntity_Credit() = runTest {
        doReturn(FAKE_WALLETENTITY_CREDIT).`when`(walletRepository).getByBroker(1)

        transactionCreditsValidateUseCase.validateTransactionUpdate(transactionEntityWithCredits, 5.0f)

        Assert.assertEquals(10.0f, transactionEntityWithCredits.credit)
    }

}