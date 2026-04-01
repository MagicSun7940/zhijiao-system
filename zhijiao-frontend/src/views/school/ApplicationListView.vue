<template>
  <el-card>
    <template #header>
      <div style="display:flex;align-items:center;gap:12px">
        <el-button :icon="ArrowLeft" circle @click="$router.back()" />
        <span>申请记录管理</span>
      </div>
    </template>

    <el-table :data="list" v-loading="loading" stripe>
      <el-table-column prop="id" label="申请ID" width="80" />
      <el-table-column prop="teacherId" label="教师ID" width="90" />
      <el-table-column prop="applyReason" label="申请说明" min-width="200" show-overflow-tooltip />
      <el-table-column label="简历" width="100">
        <template #default="{ row }">
          <el-link v-if="row.resumeUrl" type="primary" :href="row.resumeUrl" target="_blank">
            查看简历
          </el-link>
          <span v-else class="text-gray">未上传</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status]?.type">{{ statusMap[row.status]?.label }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="feedback" label="反馈意见" width="160" show-overflow-tooltip />
      <el-table-column prop="createTime" label="申请时间" width="160" />
      <el-table-column label="操作" width="170" fixed="right">
        <template #default="{ row }">
          <template v-if="row.status === 0">
            <el-button size="small" type="success" @click="openReview(row, 1)">通过</el-button>
            <el-button size="small" type="danger"  @click="openReview(row, 2)">拒绝</el-button>
          </template>
          <span v-else class="text-gray">已处理</span>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination class="mt-20" v-model:current-page="page" :page-size="10"
      :total="total" layout="total, prev, pager, next" @current-change="loadList" />

    <!-- 反馈弹窗 -->
    <el-dialog v-model="dialogVisible" :title="reviewStatus === 1 ? '通过申请' : '拒绝申请'" width="440px">
      <el-input v-model="feedback" type="textarea" :rows="3"
        :placeholder="reviewStatus === 1 ? '可填写通过意见（选填）' : '请填写拒绝原因'" />
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button :type="reviewStatus === 1 ? 'success' : 'danger'"
          :loading="submitting" @click="confirmReview">确认</el-button>
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

const loading = ref(false)
const submitting = ref(false)
const list = ref([])
const total = ref(0)
const page = ref(1)

const dialogVisible = ref(false)
const currentId = ref(null)
const reviewStatus = ref(1)
const feedback = ref('')

const statusMap = {
  0: { label: '待审核', type: 'warning' },
  1: { label: '已通过', type: 'success' },
  2: { label: '已拒绝', type: 'danger' },
}

async function loadList() {
  loading.value = true
  try {
    const res = await getPostApplications(postId, { page: page.value, size: 10 })
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

function openReview(row, status) {
  currentId.value = row.id
  reviewStatus.value = status
  feedback.value = ''
  dialogVisible.value = true
}

async function confirmReview() {
  submitting.value = true
  try {
    await reviewApplication(currentId.value, reviewStatus.value, feedback.value)
    ElMessage.success(reviewStatus.value === 1 ? '已通过' : '已拒绝')
    dialogVisible.value = false
    loadList()
  } finally {
    submitting.value = false
  }
}

onMounted(loadList)
</script>

<style scoped>
.mt-20 { margin-top: 20px; }
.text-gray { color: #999; font-size: 13px; }
</style>
