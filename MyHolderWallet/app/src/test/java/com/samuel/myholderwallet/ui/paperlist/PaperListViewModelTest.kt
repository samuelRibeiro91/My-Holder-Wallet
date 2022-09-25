package com.samuel.myholderwallet.ui.paperlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.samuel.myholderwallet.R
import com.samuel.myholderwallet.db.entity.BrokerEntity
import com.samuel.myholderwallet.db.entity.PaperEntity
import com.samuel.myholderwallet.repository.PaperRepositoryTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PaperListViewModelTest {
    private val testScheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(testScheduler)
    private val testScope = TestScope(testDispatcher)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var paperRepository: PaperRepositoryTest

    private lateinit var paperListViewModel: PaperListViewModel

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)

        paperRepository = PaperRepositoryTest()
        paperListViewModel  = PaperListViewModel(paperRepository)
    }

    @Test
    fun step1_Get_All() = testScope.runTest {
        paperListViewModel.getPapers()

        Assert.assertEquals(2, paperListViewModel.allPapersEvent.value?.size)
    }

    @Test
    fun step2_Invalid_Delete(){
        paperListViewModel.deletePaper(null)

        Assert.assertEquals(R.string.paper_error_to_delete, paperListViewModel.messageStateEventData.value)

    }

    @Test
    fun step3_Valid_Delete(){
        paperListViewModel.deletePaper(PaperEntity().apply { description = "Test" })

        Assert.assertEquals(R.string.paper_deleted_sucessfully, paperListViewModel.messageStateEventData.value)
    }
}