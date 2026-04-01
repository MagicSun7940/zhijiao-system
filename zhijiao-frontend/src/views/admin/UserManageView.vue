<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span>用户管理</span>
        <el-select v-model="query.role" placeholder="按角色筛选" clearable
          style="width:140px" @change="loadList">
          <el-option label="学校用户" :value="2" />
          <el-option label="志愿教师" :value="3" />
          <el-option label="学生"     :value="4" />
        </el-select>
      </div>
    </template>

    <el-table :data="list" v-loading="loading" stripe>
      <el-table-column prop="id"       label="ID"   width="70" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="realName" label="姓名"  width="100" />
      <el-table-column label="角色" width="100">
        <template #default="{ row }">
          <el-tag :type="roleMap[row.role]?.type" size="small">
            {{ roleMap[row.role]?.label }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-switch v-model="row.status" :active-value="1" :inactive-value="0"
            @change="toggleStatus(row)" />
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="注册时间" width="160" />
    </el-table>

    <el-pagination class="mt-20" v-model:current-page="query.page" :page-size="query.size"
      :total="total" layout="total, prev, pager, next" @current-change="loadList" />
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserList, updateUserStatus } from '@/api/user'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const query = reactive({ page: 1, size: 10, role: null })

const roleMap = {
  2: { label: '学校', type: 'success' },
  3: { label: '教师', type: 'warning' },
  4: { label: '学生', type: 'info'    },
}

async function loadList() {
  loading.value = true
  try {
    const res = await getUserList(query)
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

async function toggleStatus(row) {
  await updateUserStatus(row.id, row.status)
  ElMessage.success(row.status === 1 ? '已启用' : '已禁用')
}

onMounted(loadList)
</script>

<style scoped>
.mt-20 { margin-top: 20px; }
</style>
