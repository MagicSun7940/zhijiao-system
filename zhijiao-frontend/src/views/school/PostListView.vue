<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span>我的岗位列表</span>
        <el-button type="primary" :icon="Plus" @click="$router.push('/school/post/create')">
          发布新岗位
        </el-button>
      </div>
    </template>

    <el-table :data="list" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="岗位名称" min-width="180" />
      <el-table-column prop="courseType" label="课程类型" width="100" />
      <el-table-column prop="headcount" label="招募人数" width="90" align="center" />
      <el-table-column prop="appliedCount" label="申请人数" width="90" align="center" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status]?.type">{{ statusMap[row.status]?.label }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="startDate" label="开始日期" width="110" />
      <el-table-column prop="endDate" label="结束日期" width="110" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push(`/school/applications/${row.id}`)">
            查看申请
          </el-button>
          <el-button size="small" type="primary"
            @click="$router.push(`/school/post/edit/${row.id}`)">编辑</el-button>
          <el-popconfirm title="确认删除该岗位？" @confirm="handleDelete(row.id)">
            <template #reference>
              <el-button size="small" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
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
import { Plus } from '@element-plus/icons-vue'
import { getMyPosts, deletePost } from '@/api/post'

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
    const res = await getMyPosts(query)
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

async function handleDelete(id) {
  await deletePost(id)
  ElMessage.success('删除成功')
  loadList()
}

onMounted(loadList)
</script>

<style scoped>
.mt-20 { margin-top: 20px; }
</style>
