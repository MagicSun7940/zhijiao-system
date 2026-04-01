<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2>注册账号</h2>
        <p class="subtitle">支教管理系统</p>
      </div>

      <el-form ref="formRef" :model="form" :rules="rules" size="large" label-width="0">
        <el-form-item prop="role">
          <el-select v-model="form.role" placeholder="请选择角色" style="width:100%">
            <el-option label="志愿教师" :value="3" />
            <el-option label="学生" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item prop="realName">
          <el-input v-model="form.realName" placeholder="真实姓名" />
        </el-form-item>
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="用户名（登录账号）" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" show-password />
        </el-form-item>
        <el-form-item prop="phone">
          <el-input v-model="form.phone" placeholder="手机号" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width:100%" :loading="loading" @click="handleRegister">
            注 册
          </el-button>
        </el-form-item>
      </el-form>

      <div class="login-footer">
        已有账号？<el-link type="primary" @click="$router.push('/login')">去登录</el-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { register } from '@/api/auth'

const router = useRouter()
const formRef = ref()
const loading = ref(false)

const form = reactive({
  role: null, username: '', password: '', realName: '', phone: ''
})

const rules = {
  role: [{ required: true, message: '请选择角色' }],
  realName: [{ required: true, message: '请输入真实姓名' }],
  username: [{ required: true, message: '请输入用户名' }],
  password: [{ required: true, message: '请输入密码', min: 6, message: '密码至少6位' }],
  phone: [{ pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确' }]
}

async function handleRegister() {
  await formRef.value.validate()
  loading.value = true
  try {
    await register(form)
    ElMessage.success('注册成功，请登录')
    router.push('/login')
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
  box-shadow: 0 20px 60px rgba(0,0,0,.15);
}
.login-header { text-align: center; margin-bottom: 28px; }
.login-header h2 { font-size: 22px; color: #1a1a2e; }
.subtitle { font-size: 12px; color: #999; }
.login-footer { text-align: center; margin-top: 16px; font-size: 14px; color: #666; }
</style>
