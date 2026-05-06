<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span>用户管理</span>
        <el-select v-model="query.role" placeholder="角色筛选" clearable style="width:110px" @change="loadList">
          <el-option label="学校用户" :value="2" />
          <el-option label="志愿教师" :value="3" />
          <el-option label="学生"     :value="4" />
        </el-select>
      </div>
    </template>

    <!-- 桌面表格 -->
    <el-table :data="list" v-loading="loading" stripe class="desktop-table">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="realName" label="姓名" width="100" />
      <el-table-column label="角色" width="100">
        <template #default="{ row }">
          <el-tag :type="roleMap[row.role]?.type" size="small">{{ roleMap[row.role]?.label }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-switch v-model="row.status" :active-value="1" :inactive-value="0" @change="toggleStatus(row)" />
        </template>
      </el-table-column>
    </el-table>

    <!-- 手机卡片 -->
    <div class="mobile-list" v-loading="loading">
      <div v-for="row in list" :key="row.id" class="mobile-card">
        <div class="mc-header">
          <div>
            <div class="mc-name">{{ row.realName }}</div>
            <div class="mc-username">@{{ row.username }}</div>
          </div>
          <div style="display:flex;align-items:center;gap:8px">
            <el-tag :type="roleMap[row.role]?.type" size="small">{{ roleMap[row.role]?.label }}</el-tag>
            <el-switch v-model="row.status" :active-value="1" :inactive-value="0" @change="toggleStatus(row)" />
          </div>
        </div>
        <div class="mc-meta" v-if="row.phone">{{ row.phone }}</div>
      </div>
      <el-empty v-if="!loading && list.length === 0" description="暂无用户" />
    </div>

    <el-pagination class="mt-16" v-model:current-page="query.page" :page-size="10"
      :total="total" layout="total, prev, pager, next" @current-change="loadList" />
  </el-card>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getUserList, updateUserStatus } from '@/api/user'
const loading = ref(false), list = ref([]), total = ref(0)
const query = reactive({ page: 1, size: 10, role: null })
const roleMap = { 2:{label:'学校',type:'success'}, 3:{label:'教师',type:'warning'}, 4:{label:'学生',type:'info'} }
async function loadList() {
  loading.value = true
  try { const res = await getUserList(query); list.value = res.data?.records||[]; total.value = res.data?.total||0 }
  finally { loading.value = false }
}
async function toggleStatus(row) { await updateUserStatus(row.id, row.status); ElMessage.success(row.status===1?'已启用':'已禁用') }
onMounted(loadList)
</script>

<style scoped>
.desktop-table { display: table; }
.mobile-list   { display: none; }
.mt-16 { margin-top: 16px; }
.mobile-card { background:#fafafa; border-radius:8px; padding:12px; margin-bottom:10px; border:0.5px solid #e8e8e8; }
.mc-header { display:flex; justify-content:space-between; align-items:center; margin-bottom:4px; }
.mc-name { font-size:14px; font-weight:500; color:#1a1a2e; }
.mc-username { font-size:12px; color:#999; }
.mc-meta { font-size:12px; color:#666; }
@media (max-width: 768px) {
  .desktop-table { display: none; }
  .mobile-list   { display: block; }
}
</style>
