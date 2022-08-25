package com.samuel.myholdertransaction.db.dao

import androidx.room.*
import com.samuel.myholderwallet.db.entity.TransactionEntity
import com.samuel.myholderwallet.db.wrapper.DataTransactionsWrapperEntity
import com.samuel.myholderwallet.db.wrapper.PaperValueWrapperEntity

@Dao
interface TransactionDAO {
    @Query("SELECT * FROM `transaction` where id = :id")
    suspend fun get(id: Long): TransactionEntity

    @Query("SELECT * FROM `transaction`")
    suspend fun getAll(): List<TransactionEntity>

    @Query("SELECT * FROM `transaction` where fk_broker = :brokerID order by date desc")
    suspend fun getAllByBroker(brokerID: Long): List<TransactionEntity>

    @Query("SELECT * FROM `transaction` WHERE id IN (:transactionsIds)")
    suspend fun loadAllByIds(transactionsIds: LongArray): List<TransactionEntity>

    @Insert
    suspend fun insertAll(vararg transactions: TransactionEntity)

    @Insert
    suspend fun insert(transaction: TransactionEntity): Long

    @Update
    suspend fun update(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("select SUM(case type when 1 then quantity else quantity * -1 end)quantities from `transaction` where fk_broker = :brokerID and fk_paper = :paperID")
    suspend fun getQuantitiesOfPaperByBroker(brokerID: Long, paperID: Long): Float


    @Query( "select coalesce(SUM(case `transaction`.type when 0 then value " +
            "                    when 1 then ((value * quantity) + cost) * -1 " +
            "                    when 2 then value -cost" +
            "                    when 3 then (value -cost) * -1 " +
            "                    when 4 then ((value * quantity) - cost) " +
            "                    else 0 " +
            " end), 0)account_balance " +
            "FROM `transaction` " +
            "where fk_broker = :brokerID")
    suspend fun getAccountBalanceByBroker(brokerID: Long): Float



    @Query("select " +
            "    coalesce(SUM(case `transaction`.type " +
            "                    when 1 then ((value * quantity))  " +
            "                    when 4  then ((coalesce((select  " +
            "                                       sum(transac.value * transac.quantity) / sum(transac.quantity)average_paper " +
            "                                         FROM `transaction` transac " +
            "                                         inner join paper pp on (pp.id = transac.fk_paper) " +
            "                                         where transac.type = 1 and transac.fk_broker = `transaction`.fk_broker and pp.id = paper.id " +
            "                                        ),0) * `transaction`.quantity) * -1) " +
            "                  end), 0)total_stocks " +
            "FROM `transaction` " +
            "inner join paper on (paper.id = `transaction`.fk_paper) " +
            "where `transaction`.fk_broker = :brokerID and `transaction`.type in (1,4) and paper.type = 0")
    suspend fun getTotalStockByBroker(brokerID: Long): Float


    @Query("select " +
            "    coalesce(SUM(case `transaction`.type " +
            "                    when 1 then ((value * quantity))  " +
            "                    when 4  then ((coalesce((select  " +
            "                                       sum(transac.value * transac.quantity) / sum(transac.quantity)average_paper " +
            "                                         FROM `transaction` transac " +
            "                                         inner join paper pp on (pp.id = transac.fk_paper) " +
            "                                         where transac.type = 1 and transac.fk_broker = `transaction`.fk_broker and pp.id = paper.id " +
            "                                        ),0) * `transaction`.quantity) * -1) " +
            "                  end), 0)total_reits " +
            "FROM `transaction` " +
            "inner join paper on (paper.id = `transaction`.fk_paper) " +
            "where `transaction`.fk_broker = :brokerID and `transaction`.type in (1,4) and paper.type = 1")
    suspend fun getTotalReitsByBroker(brokerID: Long): Float

    @Query("select " +
            "    coalesce(SUM(case `transaction`.type " +
            "                    when 1 then ((value * quantity))  " +
            "                    when 4  then ((coalesce((select  " +
            "                                       sum(transac.value * transac.quantity) / sum(transac.quantity)average_paper " +
            "                                         FROM `transaction` transac " +
            "                                         inner join paper pp on (pp.id = transac.fk_paper) " +
            "                                         where transac.type = 1 and transac.fk_broker = `transaction`.fk_broker and pp.id = paper.id " +
            "                                        ),0) * `transaction`.quantity) * -1) " +
            "                  end), 0)total_adrs " +
            "FROM `transaction` " +
            "inner join paper on (paper.id = `transaction`.fk_paper) " +
            "where `transaction`.fk_broker = :brokerID and `transaction`.type in (1,4) and paper.type = 2")
    suspend fun getTotalAdrsByBroker(brokerID: Long): Float
    
    
    
    
    @Query("select " +
            "    paper.description," +
            "    coalesce(SUM(case `transaction`.type " +
            "                    when 1 then ((value * quantity))  " +
            "                    when 4  then ((coalesce((select  " +
            "                                       sum(transac.value * transac.quantity) / sum(transac.quantity)average_paper " +
            "                                         FROM `transaction` transac " +
            "                                         inner join paper pp on (pp.id = transac.fk_paper) " +
            "                                         where transac.type = 1 and transac.fk_broker = `transaction`.fk_broker and pp.id = paper.id " +
            "                                        ),0) * `transaction`.quantity) * -1) " +
            "                  end), 0)value " +
            "FROM `transaction` " +
            "inner join paper on (paper.id = `transaction`.fk_paper) " +
            "where `transaction`.fk_broker = :brokerID and `transaction`.type in (1,4) and paper.type = 0 " +
            "group by 1 " +
            "order by 2")
    suspend fun getStocksWithValues(brokerID: Long): List<PaperValueWrapperEntity>


    @Query("select " +
            "    paper.description," +
            "    coalesce(SUM(case `transaction`.type " +
            "                    when 1 then ((value * quantity))  " +
            "                    when 4  then ((coalesce((select  " +
            "                                       sum(transac.value * transac.quantity) / sum(transac.quantity)average_paper " +
            "                                         FROM `transaction` transac " +
            "                                         inner join paper pp on (pp.id = transac.fk_paper) " +
            "                                         where transac.type = 1 and transac.fk_broker = `transaction`.fk_broker and pp.id = paper.id " +
            "                                        ),0) * `transaction`.quantity) * -1) " +
            "                  end), 0)value " +
            "FROM `transaction` " +
            "inner join paper on (paper.id = `transaction`.fk_paper) " +
            "where `transaction`.fk_broker = :brokerID and `transaction`.type in (1,4) and paper.type = 1 " +
            "group by 1 " +
            "order by 2")
    suspend fun getReitsWithValues(brokerID: Long): List<PaperValueWrapperEntity>


    @Query("select " +
            "    paper.description," +
            "    coalesce(SUM(case `transaction`.type " +
            "                    when 1 then ((value * quantity))  " +
            "                    when 4  then ((coalesce((select  " +
            "                                       sum(transac.value * transac.quantity) / sum(transac.quantity)average_paper " +
            "                                         FROM `transaction` transac " +
            "                                         inner join paper pp on (pp.id = transac.fk_paper) " +
            "                                         where transac.type = 1 and transac.fk_broker = `transaction`.fk_broker and pp.id = paper.id " +
            "                                        ),0) * `transaction`.quantity) * -1) " +
            "                  end), 0)value " +
            "FROM `transaction` " +
            "inner join paper on (paper.id = `transaction`.fk_paper) " +
            "where `transaction`.fk_broker = :brokerID and `transaction`.type in (1,4) and paper.type = 2 " +
            "group by 1 " +
            "order by 2")
    suspend fun getAdrsWithValues(brokerID: Long): List<PaperValueWrapperEntity>


    @Query("select  " +
            "strftime(' %m/%Y', `transaction`.date / 1000, 'unixepoch')date, " +
            "sum(`transaction`.value)value, " +
            "(0)credits " +
            "from `transaction` " +
            "where (`transaction`.fk_broker =  :brokerID) " +
            "and  (`transaction`.type  = 2) " +
            "and  ((`transaction`.date >= :initialValue) and (`transaction`.date <= :endValue)) " +
            "group by 1")
    suspend fun getDividendsByDate(brokerID: Long, initialValue:Long, endValue: Long): List<DataTransactionsWrapperEntity>

    @Query("select  " +
            "    strftime(' %m/%Y', `transaction`.date / 1000, 'unixepoch')date, " +
            "    sum(case `transaction`.type when 1 then  `transaction`.quantity * `transaction`.value else (( `transaction`.quantity * `transaction`.value) * -10) end)value, " +
            "    sum(`transaction`.credit)credits  " +
            "from `transaction` " +
            "where (`transaction`.fk_broker =  :brokerID) " +
            "and  (`transaction`.type in (1,4)) " +
            "and  ((`transaction`.date >= :initialValue) and (`transaction`.date <= :endValue)) " +
            "group by 1 ")
    suspend fun getBuysAndUsedDividendsByDate(brokerID: Long, initialValue:Long, endValue: Long): List<DataTransactionsWrapperEntity>



}