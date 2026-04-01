<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2>支教管理系统</h2>
        <p class="subtitle">Teaching Volunteer Management System</p>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" size="large" @keyup.enter="handleLogin">
        <el-form-item prop="role">
          <el-select v-model="form.role" placeholder="请选择登录角色" style="width: 100%">
            <el-option label="管理员" :value="1" />
            <el-option label="学校用户" :value="2" />
            <el-option label="志愿教师" :value="3" />
            <el-option label="学生" :value="4" />
          </el-select>
        </el-form-item>

        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :prefix-icon="User" />
        </el-form-item>

        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码"
            :prefix-icon="Lock" show-password />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" style="width: 100%" :loading="loading" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        还没有账号？
        <el-link type="primary" @click="$router.push('/register')">立即注册</el-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref()
const loading = ref(false)

const form = reactive({
  username: '',
  password: '',
  role: null
})

const rules = {
  role: [{ required: true, message: '请选择登录角色', trigger: 'change' }],
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const rolePathMap = { 1: '/admin', 2: '/school', 3: '/teacher', 4: '/student' }

async function handleLogin() {
  await formRef.value.validate()
  loading.value = true
  try {
    const role = await userStore.login(form)
    ElMessage.success('登录成功')
    router.push(rolePathMap[role] || '/login')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #1976d2 0%, #42a5f5 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-card {
  width: 420px;
  background: #fff;
  border-radius: 12px;
  padding: 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.15);
}

.login-header {
  text-align: center;
  margin-bottom: 32px;
}

.login-header h2 {
  font-size: 24px;
  color: #1a1a2e;
  margin-bottom: 4px;
}

.subtitle {
  font-size: 12px;
  color: #999;
}

.login-footer {
  text-align: center;
  margin-top: 16px;
  color: #666;
  font-size: 14px;
}
</style>
