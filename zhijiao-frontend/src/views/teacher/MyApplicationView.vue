<template>
  <el-card header="我的申请记录">
    <el-table :data="list" v-loading="loading" stripe>
      <el-table-column prop="id" label="申请ID" width="80" />
      <el-table-column prop="postId" label="岗位ID" width="80" />
      <el-table-column prop="applyReason" label="申请说明" min-width="200" show-overflow-tooltip />
      <el-table-column label="简历" width="100">
        <template #default="{ row }">
          <el-link v-if="row.resumeUrl" type="primary" :href="row.resumeUrl" target="_blank">查看</el-link>
          <span v-else class="text-gray">无</span>
        </template>
      </el-table-column>
      <el-table-column label="审核状态" width="110">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status]?.type">{{ statusMap[row.status]?.label }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="feedback" label="学校反馈" min-width="160" show-overflow-tooltip />
      <el-table-column prop="createTime" label="申请时间" width="160" />
    </el-table>

    <el-pagination class="mt-20" v-model:current-page="page" :page-size="10"
      :total="total" layout="total, prev, pager, next" @current-change="loadList" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyApplications } from '@/api/application'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const page = ref(1)

const statusMap = {
  0: { label: '待审核', type: 'warning' },
  1: { label: '已通过', type: 'success' },
  2: { label: '已拒绝', type: 'danger' },
}

async function loadList() {
  loading.value = true
  try {
    const res = await getMyApplications({ page: page.value, size: 10 })
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

onMounted(loadList)
</script>

<style scoped>
.mt-20 { margin-top: 20px; }
.text-gray { color: #999; font-size: 13px; }
</style>
