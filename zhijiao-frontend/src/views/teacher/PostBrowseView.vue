<template>
  <div>
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <el-form inline>
        <el-form-item label="课程类型">
          <el-select v-model="query.courseType" clearable placeholder="全部类型" style="width:130px">
            <el-option v-for="t in courseTypes" :key="t" :label="t" :value="t" />
          </el-select>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="query.keyword" placeholder="岗位名称/学校/地点" clearable style="width:200px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadList">搜索</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 岗位列表 -->
    <el-row :gutter="16" class="post-grid" v-loading="loading">
      <el-col :span="8" v-for="post in list" :key="post.id" class="post-col">
        <el-card shadow="hover" class="post-card">
          <div class="post-header">
            <span class="post-title">{{ post.title }}</span>
            <el-tag size="small" type="success">{{ post.courseType }}</el-tag>
          </div>
          <div class="post-meta">
            <el-icon><Location /></el-icon> {{ post.schoolName }}
            <span style="margin-left:12px"><el-icon><Calendar /></el-icon>
              {{ post.startDate }} ~ {{ post.endDate }}
            </span>
          </div>
          <div class="post-meta">
            <el-icon><User /></el-icon> 招募 {{ post.headcount }} 人 &nbsp;|&nbsp;
            已申请 {{ post.appliedCount }} 人
          </div>
          <div class="post-desc">{{ post.description }}</div>
          <div class="post-footer">
            <el-button type="primary" size="small" @click="openApply(post)">立即申请</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-empty v-if="!loading && list.length === 0" description="暂无岗位" />

    <el-pagination class="mt-20" v-model:current-page="query.page" :page-size="query.size"
      :total="total" layout="total, prev, pager, next" @current-change="loadList" />

    <!-- 申请弹窗 -->
    <el-dialog v-model="applyVisible" title="申请岗位" width="500px">
      <p style="margin-bottom:12px;font-weight:600">{{ currentPost?.title }}</p>
      <el-form ref="applyFormRef" :model="applyForm" label-width="90px">
        <el-form-item label="申请说明" prop="applyReason"
          :rules="[{ required: true, message: '请填写申请说明' }]">
          <el-input v-model="applyForm.applyReason" type="textarea" :rows="4"
            placeholder="请介绍您的教学经历及申请理由" />
        </el-form-item>
        <el-form-item label="上传简历">
          <el-upload ref="uploadRef" :auto-upload="false" :limit="1" accept=".pdf,.doc,.docx"
            :on-change="handleFileChange">
            <el-button size="small">选择文件</el-button>
            <template #tip>
              <div class="el-upload__tip">支持 PDF / Word，不超过 10MB</div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="applyVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitting" @click="submitApply">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Location, Calendar, User } from '@element-plus/icons-vue'
import { getPostList } from '@/api/post'
import { applyPost } from '@/api/application'

const loading = ref(false)
const submitting = ref(false)
const list = ref([])
const total = ref(0)

const query = reactive({ page: 1, size: 9, courseType: '', keyword: '' })
const courseTypes = ['语文', '数学', '英语', '物理', '化学', '生物', '历史', '地理', '政治', '音乐', '美术', '体育', '信息技术']

const applyVisible = ref(false)
const currentPost = ref(null)
const applyFormRef = ref()
const applyForm = reactive({ applyReason: '' })
const resumeFile = ref(null)

async function loadList() {
  loading.value = true
  try {
    const res = await getPostList(query)
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

function reset() {
  query.courseType = ''
  query.keyword = ''
  query.page = 1
  loadList()
}

function openApply(post) {
  currentPost.value = post
  applyForm.applyReason = ''
  resumeFile.value = null
  applyVisible.value = true
}

function handleFileChange(file) {
  resumeFile.value = file.raw
}

async function submitApply() {
  await applyFormRef.value.validate()
  submitting.value = true
  try {
    const fd = new FormData()
    fd.append('postId', currentPost.value.id)
    fd.append('applyReason', applyForm.applyReason)
    if (resumeFile.value) fd.append('resume', resumeFile.value)
    await applyPost(fd)
    ElMessage.success('申请成功！')
    applyVisible.value = false
    loadList()
  } finally {
    submitting.value = false
  }
}

onMounted(loadList)
</script>

<style scoped>
.search-card { margin-bottom: 16px; }
.post-grid { margin-top: 0; }
.post-col { margin-bottom: 16px; }
.post-card { height: 100%; display: flex; flex-direction: column; }
.post-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 10px; }
.post-title { font-size: 15px; font-weight: 600; color: #1a1a2e; flex: 1; margin-right: 8px; }
.post-meta { font-size: 13px; color: #666; display: flex; align-items: center; gap: 4px; margin-bottom: 6px; }
.post-desc { font-size: 13px; color: #888; line-height: 1.6; margin: 8px 0; display: -webkit-box;
  -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.post-footer { margin-top: 12px; text-align: right; }
.mt-20 { margin-top: 20px; }
</style>
