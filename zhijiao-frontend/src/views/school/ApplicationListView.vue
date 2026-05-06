<template>
  <el-card>
    <template #header>
      <div style="display:flex;align-items:center;gap:10px">
        <el-button :icon="ArrowLeft" circle size="small" @click="$router.back()" />
        <span>申请管理</span>
      </div>
    </template>

    <!-- 桌面表格 -->
    <el-table :data="list" v-loading="loading" stripe class="desktop-table">
      <el-table-column prop="teacherId" label="教师ID" width="90" />
      <el-table-column prop="applyReason" label="申请说明" min-width="200" show-overflow-tooltip />
      <el-table-column label="简历" width="90">
        <template #default="{ row }">
          <el-link v-if="row.resumeUrl" type="primary" :href="row.resumeUrl" target="_blank">查看</el-link>
          <span v-else class="text-gray">无</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status]?.type" size="small">{{ statusMap[row.status]?.label }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <template v-if="row.status === 0">
            <el-button size="small" type="success" @click="openReview(row, 1)">通过</el-button>
            <el-button size="small" type="danger"  @click="openReview(row, 2)">拒绝</el-button>
          </template>
          <span v-else class="text-gray">已处理</span>
        </template>
      </el-table-column>
    </el-table>

    <!-- 手机卡片 -->
    <div class="mobile-list" v-loading="loading">
      <div v-for="row in list" :key="row.id" class="mobile-card">
        <div class="mc-header">
          <span class="mc-title">教师 #{{ row.teacherId }}</span>
          <el-tag size="small" :type="statusMap[row.status]?.type">{{ statusMap[row.status]?.label }}</el-tag>
        </div>
        <div class="mc-reason">{{ row.applyReason }}</div>
        <div class="mc-actions">
          <el-link v-if="row.resumeUrl" type="primary" :href="row.resumeUrl" target="_blank" style="font-size:12px">查看简历</el-link>
          <template v-if="row.status === 0">
            <el-button size="small" type="success" @click="openReview(row, 1)">通过</el-button>
            <el-button size="small" type="danger"  @click="openReview(row, 2)">拒绝</el-button>
          </template>
        </div>
      </div>
      <el-empty v-if="!loading && list.length === 0" description="暂无申请" />
    </div>

    <el-pagination class="mt-16" v-model:current-page="page" :page-size="10"
      :total="total" layout="total, prev, pager, next" @current-change="loadList" />

    <el-dialog v-model="dialogVisible" :title="reviewStatus === 1 ? '通过申请' : '拒绝申请'" width="90%" style="max-width:440px">
      <el-input v-model="feedback" type="textarea" :rows="3"
        :placeholder="reviewStatus === 1 ? '通过意见（选填）' : '请填写拒绝原因'" />
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button :type="reviewStatus === 1 ? 'success' : 'danger'" :loading="submitting" @click="confirmReview">确认</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getPostApplications, reviewApplication } from '@/api/application'
const route = useRoute()
const postId = route.params.postId
const loading = ref(false), submitting = ref(false), list = ref([]), total = ref(0), page = ref(1)
const dialogVisible = ref(false), currentId = ref(null), reviewStatus = ref(1), feedback = ref('')
const statusMap = { 0:{label:'待审核',type:'warning'}, 1:{label:'已通过',type:'success'}, 2:{label:'已拒绝',type:'danger'} }
async function loadList() {
  loading.value = true
  try { const res = await getPostApplications(postId, { page: page.value, size: 10 }); list.value = res.data?.records||[]; total.value = res.data?.total||0 }
  finally { loading.value = false }
}
function openReview(row, status) { currentId.value = row.id; reviewStatus.value = status; feedback.value = ''; dialogVisible.value = true }
async function confirmReview() {
  submitting.value = true
  try { await reviewApplication(currentId.value, reviewStatus.value, feedback.value); ElMessage.success(reviewStatus.value===1?'已通过':'已拒绝'); dialogVisible.value = false; loadList() }
  finally { submitting.value = false }
}
onMounted(loadList)
</script>

<style scoped>
.desktop-table { display: table; }
.mobile-list   { display: none; }
.mt-16 { margin-top: 16px; }
.text-gray { color:#999; font-size:13px; }
.mobile-card { background:#fafafa; border-radius:8px; padding:12px; margin-bottom:10px; border:0.5px solid #e8e8e8; }
.mc-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:6px; }
.mc-title  { font-size:14px; font-weight:500; color:#1a1a2e; }
.mc-reason { font-size:12px; color:#666; margin-bottom:8px; line-height:1.5; display:-webkit-box; -webkit-line-clamp:2; -webkit-box-orient:vertical; overflow:hidden; }
.mc-actions { display:flex; gap:8px; align-items:center; flex-wrap:wrap; }
@media (max-width: 768px) {
  .desktop-table { display: none; }
  .mobile-list   { display: block; }
}
</style>
