<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span>教学分享审核</span>
        <el-select v-model="query.status" placeholder="状态筛选" clearable style="width:110px" @change="loadList">
          <el-option label="待审核" :value="0" />
          <el-option label="已发布" :value="1" />
          <el-option label="已拒绝" :value="2" />
        </el-select>
      </div>
    </template>

    <!-- 桌面表格 -->
    <el-table :data="list" v-loading="loading" stripe class="desktop-table">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="viewCount" label="浏览量" width="80" align="center" />
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
            <el-button size="small" type="danger"  @click="audit(row.id, 2)">拒绝</el-button>
          </template>
          <span v-else class="text-gray">已处理</span>
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
        <div class="mc-meta">浏览 {{ row.viewCount }} · {{ row.createTime?.slice(0,10) }}</div>
        <div class="mc-actions" v-if="row.status === 0">
          <el-button size="small" type="success" @click="audit(row.id, 1)">通过</el-button>
          <el-button size="small" type="danger"  @click="audit(row.id, 2)">拒绝</el-button>
        </div>
        <div v-else class="text-gray">已处理</div>
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
import { getAdminShareList, auditShare } from '@/api/share'
const loading = ref(false), list = ref([]), total = ref(0)
const query = reactive({ page: 1, size: 10, status: null })
const statusMap = { 0:{label:'待审核',type:'warning'}, 1:{label:'已发布',type:'success'}, 2:{label:'已拒绝',type:'danger'} }
async function loadList() {
  loading.value = true
  try { const res = await getAdminShareList(query); list.value = res.data?.records||[]; total.value = res.data?.total||0 }
  finally { loading.value = false }
}
async function audit(id, status) { await auditShare(id, status); ElMessage.success(status===1?'已通过':'已拒绝'); loadList() }
onMounted(loadList)
</script>

<style scoped>
.desktop-table { display: table; }
.mobile-list   { display: none; }
.mt-16 { margin-top: 16px; }
.text-gray { color: #999; font-size: 13px; }
.mobile-card { background:#fafafa; border-radius:8px; padding:12px; margin-bottom:10px; border:0.5px solid #e8e8e8; }
.mc-header { display:flex; justify-content:space-between; align-items:flex-start; gap:8px; margin-bottom:6px; }
.mc-title  { font-size:14px; font-weight:500; color:#1a1a2e; flex:1; }
.mc-meta   { font-size:12px; color:#999; margin-bottom:10px; }
.mc-actions { display:flex; gap:8px; }
@media (max-width: 768px) {
  .desktop-table { display: none; }
  .mobile-list   { display: block; }
}
</style>
