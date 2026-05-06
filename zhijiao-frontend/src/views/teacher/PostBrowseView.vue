<template>
  <div>
    <!-- 搜索栏 -->
    <el-card class="search-card">
      <div class="search-row">
        <el-select v-model="query.courseType" clearable placeholder="课程类型" style="flex:1;min-width:100px">
          <el-option v-for="t in courseTypes" :key="t" :label="t" :value="t" />
        </el-select>
        <el-input v-model="query.keyword" placeholder="搜索岗位/学校" clearable style="flex:2;min-width:120px">
          <template #append><el-button @click="loadList">搜索</el-button></template>
        </el-input>
      </div>
    </el-card>

    <!-- 岗位列表 -->
    <div class="post-grid" v-loading="loading">
      <el-card v-for="post in list" :key="post.id" shadow="hover" class="post-card">
        <div class="post-header">
          <span class="post-title">{{ post.title }}</span>
          <el-tag size="small" type="success">{{ post.courseType }}</el-tag>
        </div>
        <div class="post-meta"><el-icon><Location /></el-icon> {{ post.schoolName }}</div>
        <div class="post-meta"><el-icon><Calendar /></el-icon> {{ post.startDate }} ~ {{ post.endDate }}</div>
        <div class="post-meta"><el-icon><User /></el-icon> 招募 {{ post.headcount }} 人 · 已申请 {{ post.appliedCount }} 人</div>
        <div class="post-desc">{{ post.description }}</div>
        <el-button type="primary" size="small" style="margin-top:10px;width:100%" @click="openApply(post)">立即申请</el-button>
      </el-card>
    </div>
    <el-empty v-if="!loading && list.length === 0" description="暂无岗位" />

    <el-pagination class="mt-16" v-model:current-page="query.page" :page-size="query.size"
      :total="total" layout="total, prev, pager, next" @current-change="loadList" />

    <!-- 申请弹窗 -->
    <el-dialog v-model="applyVisible" title="申请岗位" width="92%" style="max-width:500px">
      <p style="font-weight:600;margin-bottom:12px;font-size:14px">{{ currentPost?.title }}</p>
      <el-form ref="applyFormRef" :model="applyForm" label-width="80px">
        <el-form-item label="申请说明" prop="applyReason" :rules="[{required:true,message:'请填写申请说明'}]">
          <el-input v-model="applyForm.applyReason" type="textarea" :rows="4" placeholder="介绍您的教学经历及申请理由" />
        </el-form-item>
        <el-form-item label="上传简历">
          <el-upload ref="uploadRef" :auto-upload="false" :limit="1" accept=".pdf,.doc,.docx" :on-change="handleFileChange">
            <el-button size="small">选择文件</el-button>
            <template #tip><div style="font-size:12px;color:#999">PDF/Word，不超过10MB</div></template>
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
const loading = ref(false), submitting = ref(false), list = ref([]), total = ref(0)
const query = reactive({ page: 1, size: 9, courseType: '', keyword: '' })
const courseTypes = ['语文','数学','英语','物理','化学','生物','历史','地理','政治','音乐','美术','体育','信息技术']
const applyVisible = ref(false), currentPost = ref(null), applyFormRef = ref()
const applyForm = reactive({ applyReason: '' }), resumeFile = ref(null)
async function loadList() {
  loading.value = true
  try { const res = await getPostList(query); list.value = res.data?.records||[]; total.value = res.data?.total||0 }
  finally { loading.value = false }
}
function openApply(post) { currentPost.value = post; applyForm.applyReason = ''; resumeFile.value = null; applyVisible.value = true }
function handleFileChange(file) { resumeFile.value = file.raw }
async function submitApply() {
  await applyFormRef.value.validate()
  submitting.value = true
  try {
    const fd = new FormData()
    fd.append('postId', currentPost.value.id)
    fd.append('applyReason', applyForm.applyReason)
    if (resumeFile.value) fd.append('resume', resumeFile.value)
    await applyPost(fd); ElMessage.success('申请成功！'); applyVisible.value = false; loadList()
  } finally { submitting.value = false }
}
onMounted(loadList)
</script>

<style scoped>
.search-card { margin-bottom: 12px; }
.search-row { display: flex; gap: 8px; align-items: center; flex-wrap: wrap; }

.post-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}
.post-card { height: 100%; }
.post-header { display:flex; justify-content:space-between; align-items:flex-start; gap:8px; margin-bottom:8px; }
.post-title  { font-size:14px; font-weight:600; color:#1a1a2e; flex:1; }
.post-meta   { font-size:12px; color:#666; display:flex; align-items:center; gap:4px; margin-bottom:4px; }
.post-desc   { font-size:12px; color:#888; line-height:1.5; margin-top:6px;
  display:-webkit-box; -webkit-line-clamp:2; -webkit-box-orient:vertical; overflow:hidden; }
.mt-16 { margin-top: 16px; }

@media (max-width: 768px) {
  .post-grid { grid-template-columns: 1fr; }
}
</style>
