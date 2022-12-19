package com.samuel.myholderwallet.usecases

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.samuel.myholderwallet.db.entity.TransactionEntity
import com.samuel.myholderwallet.db.entity.WalletEntity
import com.samuel.myholderwallet.repository.TransactionRepository
import com.samuel.myholderwallet.repository.WalletRepository
import com.samuel.myholderwallet.types.MovementTypes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner


private val FAKE_WALLETENTITY_NOCREDIT = WalletEntity(id = 1, broker = 1, balance = 0.0f, credit = 0.0f)

private val FAKE_WALLETENTITY_CREDIT   = WalletEntity(id = 1, broker = 1, balance = 0.0f, credit = 200.0f)

private val FAKE_QUANTITY_PAPER = 100.0f

@RunWith(MockitoJUnitRunner::class)
class TransactionCreditsValidateUseCaseTest {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val testScope = TestScope(testDispatcher)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var transactionCreditsValidateUseCase : TransactionCreditsValidateUseCase

    @Mock
    private lateinit var walletRepository: WalletRepository

    @Mock
    private lateinit var transactionRepository: TransactionRepository

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

    private var cashWithDhrawalTransactionEntityWithCredits = TransactionEntity(
        id = 1,
        date = 0.0,
        credit = 5.0f,
        type = MovementTypes.CASH_WITHDRAWAL,
        cost = 0.0f,
        value = 1.0f,
        quantity = 10,
        fk_paper = 1,
        fk_broker = 1
    )

    private var inflowDividendsTransactionEntityWithCredits = TransactionEntity(
        id = 1,
        date = 0.0,
        credit = 5.0f,
        type = MovementTypes.INFLOW_DIVIDENDS,
        cost = 0.0f,
        value = 1.0f,
        quantity = 10,
        fk_paper = 1,
        fk_broker = 1
    )

    private var stockSplitTransactionEntity = TransactionEntity(
        id = 1,
        date = 0.0,
        credit = 0.0f,
        type = MovementTypes.STOCK_SPLIT,
        quantity = 0,
        factor = 3,
        fk_paper = 1,
        fk_broker = 1
    )

   @Before
    fun setup(){
       Dispatchers.setMain(testDispatcher)

        transactionCreditsValidateUseCase = TransactionCreditsValidateUseCase(walletRepository, transactionRepository)
    }

    @Test
    fun step1_insert_TransactionEntity_NoCredit() = runTest {
        doReturn(FAKE_WALLETENTITY_NOCREDIT).`when`(walletRepository).getByBroker(1)

        transactionCreditsValidateUseCase.validateTransactionInsert(transactionEntity)

        Assert.assertEquals(0.0f, transactionEntity.credit)
    }

    @Test
    fun step2_insert_TransactionEntity_Credit() = runTest {

        doReturn(FAKE_WALLETENTITY_CREDIT).`when`(walletRepository).getByBroker(1)

        transactionCreditsValidateUseCase.validateTransactionInsert(transactionEntity)

        Assert.assertEquals(5.0f, transactionEntity.credit)
    }

    @Test
    fun step3_insert_CashWithDhrawalTransactionEntity_NoCredit() = runTest {
        doReturn(FAKE_WALLETENTITY_NOCREDIT).`when`(walletRepository).getByBroker(1)

        cashWithDhrawalTransactionEntityWithCredits.credit = 0.0f

        transactionCreditsValidateUseCase.validateTransactionInsert(cashWithDhrawalTransactionEntityWithCredits)

        Assert.assertEquals(0.0f, cashWithDhrawalTransactionEntityWithCredits.credit)
    }

    @Test
    fun step4_insert_CashWithDhrawalTransactionEntity_Credit() = runTest {
        doReturn(FAKE_WALLETENTITY_CREDIT).`when`(walletRepository).getByBroker(1)

        transactionCreditsValidateUseCase.validateTransactionInsert(cashWithDhrawalTransactionEntityWithCredits)

        Assert.assertEquals(10.0f, cashWithDhrawalTransactionEntityWithCredits.credit)
    }

    @Test
    fun step5_insert_InflowDividendsTransactionEntity_NoCredit() = runTest {
        doReturn(FAKE_WALLETENTITY_NOCREDIT).`when`(walletRepository).getByBroker(1)

        transactionCreditsValidateUseCase.validateTransactionInsert(inflowDividendsTransactionEntityWithCredits)

        Assert.assertEquals(0.0f, inflowDividendsTransactionEntityWithCredits.credit)
    }

    @Test
    fun step6_insert_InflowDividendsTransactionEntity_Credit() = runTest {
        doReturn(FAKE_WALLETENTITY_CREDIT).`when`(walletRepository).getByBroker(1)

        transactionCreditsValidateUseCase.validateTransactionInsert(inflowDividendsTransactionEntityWithCredits)

        Assert.assertEquals(0.0f, inflowDividendsTransactionEntityWithCredits.credit)
    }

    ///Updates

    @Test
    fun step7_update_TransactionEntity_NoCredit() = runTest {
        doReturn(FAKE_WALLETENTITY_CREDIT).`when`(walletRepository).getByBroker(1)

        transactionCreditsValidateUseCase.validateTransactionUpdate(transactionEntity, transactionEntity.credit)

        Assert.assertEquals(5.0f, transactionEntity.credit)
    }

    @Test
    fun step8_update_TransactionEntity_Credit() = runTest {
        doReturn(FAKE_WALLETENTITY_CREDIT).`when`(walletRepository).getByBroker(1)

        transactionCreditsValidateUseCase.validateTransactionUpdate(transactionEntityWithCredits, transactionEntityWithCredits.credit)

        Assert.assertEquals(10.0f, transactionEntityWithCredits.credit)
    }

    @Test
    fun step9_update_CashWithDhrawalTransactionEntity_NoCredit() = runTest {
        doReturn(FAKE_WALLETENTITY_CREDIT).`when`(walletRepository).getByBroker(1)

        cashWithDhrawalTransactionEntityWithCredits.credit = 0.0f

        transactionCreditsValidateUseCase.validateTransactionUpdate(cashWithDhrawalTransactionEntityWithCredits, cashWithDhrawalTransactionEntityWithCredits.credit)

        Assert.assertEquals(10.0f, cashWithDhrawalTransactionEntityWithCredits.credit)
    }

    @Test
    fun step10_update_CashWithDhrawalTransactionEntity_Credit() = runTest {
        doReturn(FAKE_WALLETENTITY_CREDIT).`when`(walletRepository).getByBroker(1)

        transactionCreditsValidateUseCase.validateTransactionUpdate(cashWithDhrawalTransactionEntityWithCredits, cashWithDhrawalTransactionEntityWithCredits.credit)

        Assert.assertEquals(10.0f, cashWithDhrawalTransactionEntityWithCredits.credit)
    }

    @Test
    fun step11_update_InflowDividendsTransactionEntity_NoCredit() = runTest {
        doReturn(FAKE_WALLETENTITY_CREDIT).`when`(walletRepository).getByBroker(1)

        inflowDividendsTransactionEntityWithCredits.credit = 0.0f
        transactionCreditsValidateUseCase.validateTransactionUpdate(inflowDividendsTransactionEntityWithCredits, inflowDividendsTransactionEntityWithCredits.credit)

        Assert.assertEquals(0.0f, inflowDividendsTransactionEntityWithCredits.credit)
    }

    @Test
    fun step12_update_InflowDividendsTransactionEntity_Credit() = runTest {
        doReturn(FAKE_WALLETENTITY_CREDIT).`when`(walletRepository).getByBroker(1)

        transactionCreditsValidateUseCase.validateTransactionUpdate(inflowDividendsTransactionEntityWithCredits, inflowDividendsTransactionEntityWithCredits.credit)

        Assert.assertEquals(0.0f, inflowDividendsTransactionEntityWithCredits.credit)
    }


    @Test
    fun step13_insert_Stock_Split_InTransactionEntity() = testScope.runTest{
        doReturn(FAKE_WALLETENTITY_CREDIT).`when`(walletRepository).getByBroker(1)
        doReturn(FAKE_QUANTITY_PAPER).`when`(transactionRepository).getQuantitiesOfPaperByBroker(1, 1)

        transactionCreditsValidateUseCase.validateTransactionInsert(stockSplitTransactionEntity)

        Assert.assertEquals(200, stockSplitTransactionEntity.quantity)
    }

    @Test
    fun step14_insert_Stock_Bonus_InTransactionEntity() = testScope.runTest{
        doReturn(FAKE_WALLETENTITY_CREDIT).`when`(walletRepository).getByBroker(1)
        doReturn(FAKE_QUANTITY_PAPER).`when`(transactionRepository).getQuantitiesOfPaperByBroker(1, 1)

        stockSplitTransactionEntity.factor = 6
        stockSplitTransactionEntity.type = MovementTypes.STOCK_BONUS

        transactionCreditsValidateUseCase.validateTransactionInsert(stockSplitTransactionEntity)

        Assert.assertEquals(16, stockSplitTransactionEntity.quantity)
    }

}