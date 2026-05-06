<template>
  <div>
    <!-- 统计卡片 - 手机2列，桌面4列 -->
    <div class="stat-grid">
      <el-card shadow="hover" class="stat-card" v-for="item in stats" :key="item.label">
        <div class="stat-content">
          <el-icon :size="36" :color="item.color"><component :is="item.icon" /></el-icon>
          <div class="stat-info">
            <div class="stat-num">{{ item.value }}</div>
            <div class="stat-label">{{ item.label }}</div>
          </div>
        </div>
      </el-card>
    </div>

    <!-- 待审核区域 -->
    <div class="pending-grid">
      <el-card header="待审核岗位" shadow="hover">
        <div v-if="pendingPosts.length === 0" class="empty-tip">暂无待审核岗位</div>
        <div v-for="post in pendingPosts" :key="post.id" class="list-item">
          <div class="list-item-title">{{ post.title }}</div>
          <div class="list-item-meta">{{ post.schoolName }} · {{ post.createTime?.slice(0,10) }}</div>
          <el-button size="small" type="primary" @click="$router.push('/admin/posts')">审核</el-button>
        </div>
      </el-card>

      <el-card header="待审核分享" shadow="hover">
        <div v-if="pendingShares.length === 0" class="empty-tip">暂无待审核分享</div>
        <div v-for="share in pendingShares" :key="share.id" class="list-item">
          <div class="list-item-title">{{ share.title }}</div>
          <div class="list-item-meta">{{ share.createTime?.slice(0,10) }}</div>
          <el-button size="small" type="primary" @click="$router.push('/admin/shares')">审核</el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const stats = [
  { label: '注册用户', value: 128, icon: 'User',         color: '#409eff' },
  { label: '发布岗位', value: 36,  icon: 'Document',     color: '#67c23a' },
  { label: '申请总数', value: 89,  icon: 'List',         color: '#e6a23c' },
  { label: '教学分享', value: 47,  icon: 'ChatDotRound', color: '#f56c6c' },
]
const pendingPosts  = ref([])
const pendingShares = ref([])
</script>

<style scoped>
.stat-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin-bottom: 16px;
}
.stat-content { display: flex; align-items: center; gap: 12px; }
.stat-num { font-size: 24px; font-weight: bold; color: #1a1a2e; }
.stat-label { font-size: 12px; color: #999; margin-top: 2px; }

.pending-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 12px;
}

.list-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 0;
  border-bottom: 0.5px solid #f0f0f0;
}
.list-item:last-child { border-bottom: none; }
.list-item-title { flex: 1; font-size: 13px; color: #333; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.list-item-meta { font-size: 12px; color: #999; white-space: nowrap; }
.empty-tip { font-size: 13px; color: #999; text-align: center; padding: 20px 0; }

@media (max-width: 768px) {
  .stat-grid { grid-template-columns: repeat(2, 1fr); }
  .pending-grid { grid-template-columns: 1fr; }
  .list-item-meta { display: none; }
}
</style>
