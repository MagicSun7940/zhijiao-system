<template>
  <el-card>
    <template #header><span>岗位审核管理</span></template>

    <!-- 桌面端表格 -->
    <el-table :data="list" v-loading="loading" stripe class="desktop-table">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="岗位名称" min-width="160" />
      <el-table-column prop="schoolName" label="发布学校" width="140" />
      <el-table-column prop="courseType" label="课程类型" width="100" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status]?.type">{{ statusMap[row.status]?.label }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <template v-if="row.status === 0">
            <el-button size="small" type="success" @click="audit(row.id, 1)">通过</el-button>
            <el-button size="small" type="danger"  @click="audit(row.id, 3)">拒绝</el-button>
          </template>
          <span v-else class="text-gray">已处理</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 手机端卡片列表 -->
    <div class="mobile-list" v-loading="loading">
      <div v-for="row in list" :key="row.id" class="mobile-card">
        <div class="mc-header">
          <span class="mc-title">{{ row.title }}</span>
          <el-tag size="small" :type="statusMap[row.status]?.type">{{ statusMap[row.status]?.label }}</el-tag>
        </div>
        <div class="mc-meta">{{ row.schoolName }} · {{ row.courseType }}</div>
        <div class="mc-actions" v-if="row.status === 0">
          <el-button size="small" type="success" @click="audit(row.id, 1)">通过</el-button>
          <el-button size="small" type="danger"  @click="audit(row.id, 3)">拒绝</el-button>
        </div>
        <div v-else class="text-gray mc-done">已处理</div>
      </div>
      <el-empty v-if="!loading && list.length === 0" description="暂无数据" />
    </div>

    <el-pagination class="mt-16" v-model:current-page="query.page" :page-size="query.size"
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
  } finally { loading.value = false }
}
async function audit(id, status) {
  await auditPost(id, status)
  ElMessage.success(status === 1 ? '已通过' : '已拒绝')
  loadList()
}
onMounted(loadList)
</script>

<style scoped>
.desktop-table { display: table; }
.mobile-list   { display: none; }
.mt-16 { margin-top: 16px; }
.text-gray { color: #999; font-size: 13px; }

.mobile-card {
  background: #fafafa;
  border-radius: 8px;
  padding: 12px;
  margin-bottom: 10px;
  border: 0.5px solid #e8e8e8;
}
.mc-header { display: flex; justify-content: space-between; align-items: flex-start; gap: 8px; margin-bottom: 6px; }
.mc-title  { font-size: 14px; font-weight: 500; color: #1a1a2e; flex: 1; }
.mc-meta   { font-size: 12px; color: #999; margin-bottom: 10px; }
.mc-actions { display: flex; gap: 8px; }
.mc-done   { font-size: 12px; }

@media (max-width: 768px) {
  .desktop-table { display: none; }
  .mobile-list   { display: block; }
}
</style>
