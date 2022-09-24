package com.samuel.myholderwallet.ui.broker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.entity.BrokerEntity
import com.samuel.myholderwallet.repository.BrokerRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import net.bytebuddy.implementation.bytecode.Throw
import org.junit.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn


private const val FAKE_BROKER_INSERTED: Long = 1

private val FAKE_BROKER = BrokerEntity().apply {
    name = "Broker Test"
}

@RunWith(MockitoJUnitRunner::class)
class BrokerViewModelTest {

    val testScheduler = TestCoroutineScheduler()
    val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    val testScope = TestScope(testDispatcher)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    @Mock
    private lateinit var brokerRepository: BrokerRepository

    private lateinit var brokerViewModel: BrokerViewModel

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)

        brokerViewModel = BrokerViewModel(brokerRepository)
    }


    @Test
    fun step1_insert_a_broker_invalid() = testScope.runTest {
        val brokerName = ""

        brokerViewModel.insertOrUpdateBroker(brokerName)

        Assert.assertEquals(
            R.string.broker_error_to_insert,
            brokerViewModel.messageStateEventData.value
        )
    }


    @Test
    fun step2_insert_a_broker_valid() = testScope.runTest {
        val name = "Broker Test"

        `when`(brokerRepository.insert(FAKE_BROKER)).doReturn(FAKE_BROKER_INSERTED)

        brokerViewModel.insertOrUpdateBroker(name)

        Assert.assertEquals(
            R.string.broker_inserted_sucessfully,
            brokerViewModel.messageStateEventData.value
        )
    }

    @Test
    fun step3_update_a_broker_valid() = testScope.runTest{
        val brokerName = "Broker Test"
        val brokerID = 2L

        `when`(brokerRepository.update(
            BrokerEntity().apply {
                this.id   = brokerID
                this.name = brokerName })).doReturn(Unit)

        brokerViewModel.insertOrUpdateBroker(brokerName, brokerID)

        Assert.assertEquals(
            R.string.broker_updated_sucessfully,
            brokerViewModel.messageStateEventData.value
        )
    }
}




