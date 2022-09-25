package com.samuel.myholderwallet.ui.paper

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.samuel.myholderwallet.repository.PaperRepositoryTest
import com.samuel.myholderwallet.types.PaperType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import com.samuel.myholderwallet.R

@RunWith(MockitoJUnitRunner::class)
class PaperViewModelTest {

    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val testScope = TestScope(testDispatcher)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var paperRepository: PaperRepositoryTest

    private lateinit var paperViewModel: PaperViewModel

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)

        paperRepository = PaperRepositoryTest()
        paperViewModel  = PaperViewModel(paperRepository)
    }

    @Test
    fun step1_insert_a_paper_invalid() = testScope.runTest {

        paperViewModel.addOrUpdatePaper("TEST1", "Test Entertainment", PaperType.STOCK)

        Assert.assertEquals(R.string.paper_error_to_insert, paperViewModel.messageStateEventData.value)
    }

    @Test
    fun step2_insert_a_paper_valid() = testScope.runTest {
        paperViewModel.addOrUpdatePaper("TEST12", "Test Entertainment", PaperType.STOCK)

        Assert.assertEquals(R.string.paper_inserted_sucessfully, paperViewModel.messageStateEventData.value)
    }

    @Test
    fun step3_update_a_paper_invalid() = testScope.runTest {
        paperViewModel.addOrUpdatePaper("TEST1", "Test Entertainment", PaperType.STOCK, 1)

        Assert.assertEquals(R.string.paper_error_to_update, paperViewModel.messageStateEventData.value)
    }

    @Test
    fun step4_update_a_paper_valid() = testScope.runTest {
        paperViewModel.addOrUpdatePaper("TEST12", "Test Entertainment", PaperType.STOCK, 1)

        Assert.assertEquals(R.string.paper_updated_sucessfully, paperViewModel.messageStateEventData.value)
    }
}