<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span>我的岗位</span>
        <el-button type="primary" size="small" :icon="Plus" @click="$router.push('/school/post/create')">发布</el-button>
      </div>
    </template>

    <!-- 桌面表格 -->
    <el-table :data="list" v-loading="loading" stripe class="desktop-table">
      <el-table-column prop="title" label="岗位名称" min-width="180" />
      <el-table-column prop="courseType" label="课程" width="90" />
      <el-table-column prop="headcount" label="人数" width="70" align="center" />
      <el-table-column prop="appliedCount" label="申请" width="70" align="center" />
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status]?.type" size="small">{{ statusMap[row.status]?.label }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push(`/school/applications/${row.id}`)">申请</el-button>
          <el-button size="small" type="primary" @click="$router.push(`/school/post/edit/${row.id}`)">编辑</el-button>
          <el-popconfirm title="确认删除？" @confirm="handleDelete(row.id)">
            <template #reference><el-button size="small" type="danger">删除</el-button></template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <!-- 手机卡片 -->
    <div class="mobile-list" v-loading="loading">
      <div v-for="row in list" :key="row.id" class="mobile-card">
        <div class="mc-header">
          <span class="mc-title">{{ row.title }}</span>
          <el-tag size="small" :type="statusMap[row.status]?.type">{{ statusMap[row.status]?.label }}</el-tag>
        </div>
        <div class="mc-meta">{{ row.courseType }} · 招募{{ row.headcount }}人 · 已申请{{ row.appliedCount }}人</div>
        <div class="mc-meta">{{ row.startDate }} ~ {{ row.endDate }}</div>
        <div class="mc-actions">
          <el-button size="small" @click="$router.push(`/school/applications/${row.id}`)">查看申请</el-button>
          <el-button size="small" type="primary" @click="$router.push(`/school/post/edit/${row.id}`)">编辑</el-button>
          <el-popconfirm title="确认删除？" @confirm="handleDelete(row.id)">
            <template #reference><el-button size="small" type="danger">删除</el-button></template>
          </el-popconfirm>
        </div>
      </div>
      <el-empty v-if="!loading && list.length === 0" description="暂无岗位" />
    </div>

    <el-pagination class="mt-16" v-model:current-page="query.page" :page-size="query.size"
      :total="total" layout="total, prev, pager, next" @current-change="loadList" />
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getMyPosts, deletePost } from '@/api/post'
const loading = ref(false), list = ref([]), total = ref(0)
const query = reactive({ page: 1, size: 10 })
const statusMap = { 0:{label:'待审核',type:'warning'}, 1:{label:'已发布',type:'success'}, 2:{label:'已关闭',type:'info'}, 3:{label:'已拒绝',type:'danger'} }
async function loadList() {
  loading.value = true
  try { const res = await getMyPosts(query); list.value = res.data?.records||[]; total.value = res.data?.total||0 }
  finally { loading.value = false }
}
async function handleDelete(id) { await deletePost(id); ElMessage.success('删除成功'); loadList() }
onMounted(loadList)
</script>

<style scoped>
.desktop-table { display: table; }
.mobile-list   { display: none; }
.mt-16 { margin-top: 16px; }
.mobile-card { background:#fafafa; border-radius:8px; padding:12px; margin-bottom:10px; border:0.5px solid #e8e8e8; }
.mc-header { display:flex; justify-content:space-between; align-items:flex-start; gap:8px; margin-bottom:6px; }
.mc-title  { font-size:14px; font-weight:500; color:#1a1a2e; flex:1; }
.mc-meta   { font-size:12px; color:#999; margin-bottom:4px; }
.mc-actions { display:flex; gap:6px; margin-top:8px; flex-wrap:wrap; }
@media (max-width: 768px) {
  .desktop-table { display: none; }
  .mobile-list   { display: block; }
}
</style>
