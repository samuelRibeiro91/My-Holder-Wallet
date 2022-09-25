package com.samuel.myholderwallet.repository

import com.samuel.myholderwallet.db.entity.PaperEntity

class PaperRepositoryTest: PaperRepository {
    override suspend fun get(id: Long): PaperEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getAll(): List<PaperEntity>  = listOf(
        PaperEntity().apply {
            description = "test 1"
        },
        PaperEntity().apply {
            description = "test 2 "
        }
    )

    override suspend fun loadAllByIds(papersIds: LongArray): List<PaperEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun insertAll(vararg papers: PaperEntity) {
        TODO("Not yet implemented")
    }

    override suspend fun insert(paper: PaperEntity): Long {
        if (paper.initial == "TEST12") return 1 else throw Exception("Erro ao inserir papel")
    }

    override suspend fun update(paper: PaperEntity) {
        if (paper.initial != "TEST12") throw Exception("Erro ao inserir papel")
    }

    override suspend fun delete(paper: PaperEntity) {

    }
}