<template>
  <div>
    <el-card class="welcome-card">
      <div class="welcome-content">
        <el-icon :size="48" color="#409eff"><Reading /></el-icon>
        <div class="welcome-text">
          <h2>欢迎使用支教管理系统</h2>
          <p>您好，{{ userStore.realName }}！在这里可以查看任课教师的课程安排和教学资源。</p>
        </div>
      </div>
    </el-card>

    <el-row :gutter="20" style="margin-top:20px">
      <el-col :span="16">
        <el-card header="最新教学资源" shadow="hover">
          <el-list v-loading="loading">
            <el-card v-for="share in latestShares" :key="share.id" shadow="never"
              class="share-item" @click="$router.push('/student/share')">
              <div class="share-row">
                <span class="share-title">{{ share.title }}</span>
                <div class="share-meta">
                  <el-icon><View /></el-icon> {{ share.viewCount }}
                  <span style="margin-left:12px">{{ share.createTime?.slice(0, 10) }}</span>
                </div>
              </div>
            </el-card>
          </el-list>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card header="支教公告" shadow="hover">
          <el-empty description="暂无公告" :image-size="80" />
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Reading, View } from '@element-plus/icons-vue'
import { useUserStore } from '@/store/user'
import { getShareList } from '@/api/share'

const userStore = useUserStore()
const loading = ref(false)
const latestShares = ref([])

async function loadShares() {
  loading.value = true
  try {
    const res = await getShareList({ page: 1, size: 5 })
    latestShares.value = res.data?.records || []
  } finally {
    loading.value = false
  }
}

onMounted(loadShares)
</script>

<style scoped>
.welcome-card { background: linear-gradient(135deg, #e8f4fd, #f0f9ff); }
.welcome-content { display: flex; align-items: center; gap: 20px; }
.welcome-text h2 { font-size: 20px; color: #1a1a2e; margin-bottom: 6px; }
.welcome-text p { color: #666; font-size: 14px; }
.share-item { margin-bottom: 10px; cursor: pointer; transition: box-shadow .2s; }
.share-item:hover { box-shadow: 0 2px 12px rgba(0,0,0,.1); }
.share-row { display: flex; justify-content: space-between; align-items: center; }
.share-title { font-size: 14px; color: #1a1a2e; font-weight: 500; }
.share-meta { font-size: 12px; color: #999; display: flex; align-items: center; gap: 4px; }
</style>
