package com.samuel.myholderwallet.ui.broker

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.repository.BrokerRepositoryTest
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.junit.Before
import org.junit.Test
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BrokerViewModelTest {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val testScope = TestScope(testDispatcher)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var brokerRepository: BrokerRepositoryTest

    private lateinit var brokerViewModel: BrokerViewModel

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)

        brokerRepository = BrokerRepositoryTest()
        brokerViewModel  = BrokerViewModel(brokerRepository)
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

        brokerViewModel.insertOrUpdateBroker(brokerName, brokerID)

        Assert.assertEquals(
            R.string.broker_updated_sucessfully,
            brokerViewModel.messageStateEventData.value
        )
    }

    @Test
    fun step4_update_a_broker_invalid() = testScope.runTest{
        val brokerName = "Broker Test Error"
        val brokerID = 2L

        brokerViewModel.insertOrUpdateBroker(brokerName, brokerID)

        Assert.assertEquals(
            R.string.broker_error_to_update,
            brokerViewModel.messageStateEventData.value
        )
    }
}




