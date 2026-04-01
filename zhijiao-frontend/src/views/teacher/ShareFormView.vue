<template>
  <el-card header="发布教学分享">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="90px" style="max-width:720px">
      <el-form-item label="分享标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入分享标题" />
      </el-form-item>

      <el-form-item label="正文内容" prop="content">
        <el-input v-model="form.content" type="textarea" :rows="10"
          placeholder="请输入教学心得、经验分享等内容..." />
      </el-form-item>

      <el-form-item label="附件图片">
        <el-upload :action="uploadAction" list-type="picture-card" :limit="6"
          :headers="uploadHeaders" :on-success="handleUploadSuccess" :on-remove="handleRemove"
          accept="image/*,video/*">
          <el-icon><Plus /></el-icon>
          <template #tip>
            <div class="el-upload__tip">支持图片和视频，最多6个文件</div>
          </template>
        </el-upload>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" :loading="loading" @click="handleSubmit">提交发布</el-button>
        <el-button @click="$router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { publishShare } from '@/api/share'

const router = useRouter()
const formRef = ref()
const loading = ref(false)
const attachmentUrls = ref([])

const uploadAction = '/api/common/upload'
const uploadHeaders = computed(() => ({
  Authorization: `Bearer ${localStorage.getItem('token')}`
}))

const form = reactive({ title: '', content: '' })
const rules = {
  title: [{ required: true, message: '请输入标题' }],
  content: [{ required: true, message: '请输入正文内容' }],
}

function handleUploadSuccess(res) {
  if (res.code === 200) {
    attachmentUrls.value.push(res.data)
  }
}

function handleRemove(file) {
  const url = file.response?.data
  if (url) {
    attachmentUrls.value = attachmentUrls.value.filter(u => u !== url)
  }
}

async function handleSubmit() {
  await formRef.value.validate()
  loading.value = true
  try {
    await publishShare({
      ...form,
      attachments: JSON.stringify(attachmentUrls.value)
    })
    ElMessage.success('发布成功，等待管理员审核')
    router.push('/teacher/share')
  } finally {
    loading.value = false
  }
}
</script>
