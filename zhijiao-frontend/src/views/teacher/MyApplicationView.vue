<template>
  <el-card header="我的申请记录">
    <!-- 桌面表格 -->
    <el-table :data="list" v-loading="loading" stripe class="desktop-table">
      <el-table-column prop="postId" label="岗位ID" width="80" />
      <el-table-column prop="applyReason" label="申请说明" min-width="200" show-overflow-tooltip />
      <el-table-column label="简历" width="80">
        <template #default="{ row }">
          <el-link v-if="row.resumeUrl" type="primary" :href="row.resumeUrl" target="_blank">查看</el-link>
          <span v-else class="text-gray">无</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status]?.type" size="small">{{ statusMap[row.status]?.label }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="feedback" label="反馈" min-width="140" show-overflow-tooltip />
    </el-table>

    <!-- 手机卡片 -->
    <div class="mobile-list" v-loading="loading">
      <div v-for="row in list" :key="row.id" class="mobile-card">
        <div class="mc-header">
          <span class="mc-title">岗位 #{{ row.postId }}</span>
          <el-tag size="small" :type="statusMap[row.status]?.type">{{ statusMap[row.status]?.label }}</el-tag>
        </div>
        <div class="mc-reason">{{ row.applyReason }}</div>
        <div class="mc-feedback" v-if="row.feedback">反馈：{{ row.feedback }}</div>
        <el-link v-if="row.resumeUrl" type="primary" :href="row.resumeUrl" target="_blank" style="font-size:12px">查看简历</el-link>
      </div>
      <el-empty v-if="!loading && list.length === 0" description="暂无申请记录" />
    </div>

    <el-pagination class="mt-16" v-model:current-page="page" :page-size="10"
      :total="total" layout="total, prev, pager, next" @current-change="loadList" />
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getMyApplications } from '@/api/application'
const loading = ref(false), list = ref([]), total = ref(0), page = ref(1)
const statusMap = { 0:{label:'待审核',type:'warning'}, 1:{label:'已通过',type:'success'}, 2:{label:'已拒绝',type:'danger'} }
async function loadList() {
  loading.value = true
  try { const res = await getMyApplications({ page: page.value, size: 10 }); list.value = res.data?.records||[]; total.value = res.data?.total||0 }
  finally { loading.value = false }
}
onMounted(loadList)
</script>

<style scoped>
.desktop-table { display: table; }
.mobile-list   { display: none; }
.mt-16 { margin-top: 16px; }
.text-gray { color:#999; font-size:13px; }
.mobile-card { background:#fafafa; border-radius:8px; padding:12px; margin-bottom:10px; border:0.5px solid #e8e8e8; }
.mc-header   { display:flex; justify-content:space-between; align-items:center; margin-bottom:6px; }
.mc-title    { font-size:14px; font-weight:500; color:#1a1a2e; }
.mc-reason   { font-size:12px; color:#666; line-height:1.5; margin-bottom:6px; }
.mc-feedback { font-size:12px; color:#e6a23c; margin-bottom:6px; }
@media (max-width: 768px) {
  .desktop-table { display: none; }
  .mobile-list   { display: block; }
}
</style>
