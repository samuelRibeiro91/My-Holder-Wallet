package com.samuel.myholderwallet.ui.brokerlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.entity.BrokerEntity
import com.samuel.myholderwallet.repository.BrokerRepositoryTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BrokerListViewModelTest {
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val testScope = TestScope(testDispatcher)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var brokerRepository: BrokerRepositoryTest

    private lateinit var brokerListViewModel: BrokerListViewModel

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)

        brokerRepository = BrokerRepositoryTest()
        brokerListViewModel  = BrokerListViewModel(brokerRepository)
    }

    @Test
    fun step1_Get_All() = testScope.runTest {
        brokerListViewModel.getBrokers()

        Assert.assertEquals(2, brokerListViewModel.allBrokersEvent.value?.size)
    }

    @Test
    fun step2_Invalid_Delete(){
        brokerListViewModel.deleteBroker(null)

        Assert.assertEquals(R.string.broker_error_to_delete, brokerListViewModel.messageStateEventData.value)

    }

    @Test
    fun step3_Valid_Delete(){
        brokerListViewModel.deleteBroker(BrokerEntity().apply { name = "Test" })

        Assert.assertEquals(R.string.broker_deleted_sucessfully, brokerListViewModel.messageStateEventData.value)
    }
}