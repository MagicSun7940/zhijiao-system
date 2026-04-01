<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span>我的教学分享</span>
        <el-button type="primary" @click="$router.push('/teacher/share/create')">发布分享</el-button>
      </div>
    </template>

    <el-table :data="list" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="200" />
      <el-table-column prop="viewCount" label="浏览量" width="90" align="center" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status]?.type">{{ statusMap[row.status]?.label }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="发布时间" width="160" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-popconfirm title="确认删除？" @confirm="handleDelete(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination class="mt-20" v-model:current-page="page" :page-size="10"
      :total="total" layout="total, prev, pager, next" @current-change="loadList" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getMyShares, deleteShare } from '@/api/share'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const page = ref(1)

const statusMap = {
  0: { label: '待审核', type: 'warning' },
  1: { label: '已发布', type: 'success' },
  2: { label: '已拒绝', type: 'danger' },
}

async function loadList() {
  loading.value = true
  try {
    const res = await getMyShares({ page: page.value, size: 10 })
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

async function handleDelete(id) {
  await deleteShare(id)
  ElMessage.success('删除成功')
  loadList()
}

onMounted(loadList)
</script>
<style scoped>
.mt-20 { margin-top: 20px; }
</style>
