<template>
  <el-card>
    <template #header>
      <span>岗位审核管理</span>
    </template>

    <el-table :data="list" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="岗位名称" min-width="160" />
      <el-table-column prop="schoolName" label="发布学校" width="140" />
      <el-table-column prop="courseType" label="课程类型" width="100" />
      <el-table-column prop="headcount" label="招募人数" width="90" align="center" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status]?.type">{{ statusMap[row.status]?.label }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="提交时间" width="160" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <template v-if="row.status === 0">
            <el-button size="small" type="success" @click="audit(row.id, 1)">通过</el-button>
            <el-button size="small" type="danger" @click="audit(row.id, 3)">拒绝</el-button>
          </template>
          <span v-else class="text-gray">已处理</span>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination class="mt-20" v-model:current-page="query.page" :page-size="query.size"
      :total="total" layout="total, prev, pager, next" @current-change="loadList" />
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAdminPostList, auditPost } from '@/api/post'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = reactive({ page: 1, size: 10 })

const statusMap = {
  0: { label: '待审核', type: 'warning' },
  1: { label: '已发布', type: 'success' },
  2: { label: '已关闭', type: 'info' },
  3: { label: '已拒绝', type: 'danger' },
}

async function loadList() {
  loading.value = true
  try {
    const res = await getAdminPostList(query)
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

async function audit(id, status) {
  await auditPost(id, status)
  ElMessage.success(status === 1 ? '已通过' : '已拒绝')
  loadList()
}

onMounted(loadList)
</script>

<style scoped>
.mt-20 { margin-top: 20px; }
.text-gray { color: #999; font-size: 13px; }
</style>
