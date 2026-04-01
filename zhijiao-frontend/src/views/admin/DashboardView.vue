<template>
  <div>
    <el-row :gutter="20" class="stat-row">
      <el-col :span="6" v-for="item in stats" :key="item.label">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <el-icon :size="40" :color="item.color"><component :is="item.icon" /></el-icon>
            <div class="stat-info">
              <div class="stat-num">{{ item.value }}</div>
              <div class="stat-label">{{ item.label }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card header="待审核岗位" shadow="hover">
          <el-table :data="pendingPosts" size="small">
            <el-table-column prop="title" label="岗位名称" />
            <el-table-column prop="schoolName" label="学校" width="120" />
            <el-table-column prop="createTime" label="提交时间" width="160" />
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-button size="small" type="primary" @click="$router.push('/admin/posts')">审核</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card header="待审核分享" shadow="hover">
          <el-table :data="pendingShares" size="small">
            <el-table-column prop="title" label="分享标题" />
            <el-table-column prop="createTime" label="提交时间" width="160" />
            <el-table-column label="操作" width="120">
              <template #default>
                <el-button size="small" type="primary" @click="$router.push('/admin/shares')">审核</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 模拟统计数据（实际对接后端统计接口）
const stats = [
  { label: '注册用户', value: 128, icon: 'User', color: '#409eff' },
  { label: '发布岗位', value: 36, icon: 'Document', color: '#67c23a' },
  { label: '申请总数', value: 89, icon: 'List', color: '#e6a23c' },
  { label: '教学分享', value: 47, icon: 'ChatDotRound', color: '#f56c6c' },
]
const pendingPosts = ref([])
const pendingShares = ref([])
</script>

<style scoped>
.stat-card { cursor: default; }
.stat-content { display: flex; align-items: center; gap: 16px; }
.stat-num { font-size: 28px; font-weight: bold; color: #1a1a2e; }
.stat-label { font-size: 13px; color: #999; margin-top: 4px; }
</style>
