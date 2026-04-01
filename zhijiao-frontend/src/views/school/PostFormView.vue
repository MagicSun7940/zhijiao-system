<template>
  <el-card :header="isEdit ? '编辑岗位' : '发布新岗位'">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="100px" style="max-width:680px">
      <el-form-item label="岗位标题" prop="title">
        <el-input v-model="form.title" placeholder="请输入岗位标题" />
      </el-form-item>

      <el-form-item label="课程类型" prop="courseType">
        <el-select v-model="form.courseType" placeholder="请选择课程类型" style="width:100%">
          <el-option v-for="t in courseTypes" :key="t" :label="t" :value="t" />
        </el-select>
      </el-form-item>

      <el-form-item label="招募人数" prop="headcount">
        <el-input-number v-model="form.headcount" :min="1" :max="100" />
      </el-form-item>

      <el-form-item label="支教时间" prop="dateRange">
        <el-date-picker v-model="form.dateRange" type="daterange" start-placeholder="开始日期"
          end-placeholder="结束日期" value-format="YYYY-MM-DD" style="width:100%" />
      </el-form-item>

      <el-form-item label="支教地点" prop="address">
        <el-input v-model="form.address" placeholder="请输入支教地点" />
      </el-form-item>

      <el-form-item label="岗位描述" prop="description">
        <el-input v-model="form.description" type="textarea" :rows="5"
          placeholder="请详细描述岗位要求、工作内容等" />
      </el-form-item>

      <el-form-item>
        <el-button type="primary" :loading="loading" @click="handleSubmit">
          {{ isEdit ? '保存修改' : '提交发布' }}
        </el-button>
        <el-button @click="$router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { publishPost, updatePost, getPostDetail } from '@/api/post'

const route = useRoute()
const router = useRouter()
const formRef = ref()
const loading = ref(false)

const isEdit = computed(() => !!route.params.id)

const courseTypes = ['语文', '数学', '英语', '物理', '化学', '生物', '历史', '地理', '政治', '音乐', '美术', '体育', '信息技术']

const form = reactive({
  title: '',
  courseType: '',
  headcount: 1,
  dateRange: [],
  address: '',
  description: '',
  categoryId: 1
})

const rules = {
  title: [{ required: true, message: '请输入岗位标题' }],
  courseType: [{ required: true, message: '请选择课程类型' }],
  headcount: [{ required: true, message: '请输入招募人数' }],
  dateRange: [{ required: true, message: '请选择支教时间' }],
  description: [{ required: true, message: '请填写岗位描述' }],
}

async function handleSubmit() {
  await formRef.value.validate()
  loading.value = true
  try {
    const payload = {
      ...form,
      startDate: form.dateRange[0],
      endDate: form.dateRange[1],
    }
    delete payload.dateRange

    if (isEdit.value) {
      await updatePost(route.params.id, payload)
      ElMessage.success('修改成功，等待重新审核')
    } else {
      await publishPost(payload)
      ElMessage.success('发布成功，等待管理员审核')
    }
    router.push('/school/posts')
  } finally {
    loading.value = false
  }
}

onMounted(async () => {
  if (isEdit.value) {
    const res = await getPostDetail(route.params.id)
    const d = res.data
    Object.assign(form, d)
    form.dateRange = [d.startDate, d.endDate]
  }
})
</script>
