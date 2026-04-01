<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span>教学资源浏览</span>
        <el-input v-model="keyword" placeholder="搜索标题" clearable style="width:220px"
          @keyup.enter="loadList" @clear="loadList">
          <template #append>
            <el-button :icon="Search" @click="loadList" />
          </template>
        </el-input>
      </div>
    </template>

    <el-row :gutter="16" v-loading="loading">
      <el-col :span="12" v-for="share in list" :key="share.id" style="margin-bottom:16px">
        <el-card shadow="hover" class="share-card" @click="openDetail(share)">
          <div class="share-title">{{ share.title }}</div>
          <div class="share-content">{{ share.content }}</div>
          <div class="share-footer">
            <span><el-icon><View /></el-icon> {{ share.viewCount }} 次浏览</span>
            <span>{{ share.createTime?.slice(0, 10) }}</span>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-empty v-if="!loading && list.length === 0" description="暂无教学资源" />

    <el-pagination class="mt-20" v-model:current-page="page" :page-size="10"
      :total="total" layout="total, prev, pager, next" @current-change="loadList" />

    <!-- 详情弹窗 -->
    <el-dialog v-model="detailVisible" :title="currentShare?.title" width="680px" top="5vh">
      <div class="detail-content">{{ currentShare?.content }}</div>
      <div v-if="currentShare?.attachments" class="attachments">
        <p style="font-weight:600;margin-bottom:8px">附件资料：</p>
        <el-image v-for="(url, i) in JSON.parse(currentShare.attachments || '[]')"
          :key="i" :src="url" :preview-src-list="JSON.parse(currentShare.attachments || '[]')"
          style="width:120px;height:90px;margin-right:8px;object-fit:cover;border-radius:4px" />
      </div>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Search, View } from '@element-plus/icons-vue'
import { getShareList, getShareDetail } from '@/api/share'

const loading = ref(false)
const list = ref([])
const total = ref(0)
const page = ref(1)
const keyword = ref('')

const detailVisible = ref(false)
const currentShare = ref(null)

async function loadList() {
  loading.value = true
  try {
    const res = await getShareList({ page: page.value, size: 10, keyword: keyword.value })
    list.value = res.data?.records || []
    total.value = res.data?.total || 0
  } finally {
    loading.value = false
  }
}

async function openDetail(share) {
  const res = await getShareDetail(share.id)
  currentShare.value = res.data
  detailVisible.value = true
}

onMounted(loadList)
</script>

<style scoped>
.share-card { cursor: pointer; height: 100%; transition: transform .2s; }
.share-card:hover { transform: translateY(-2px); }
.share-title { font-size: 15px; font-weight: 600; color: #1a1a2e; margin-bottom: 8px; }
.share-content { font-size: 13px; color: #666; line-height: 1.6; display: -webkit-box;
  -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden; }
.share-footer { display: flex; justify-content: space-between; margin-top: 12px;
  font-size: 12px; color: #999; align-items: center; }
.detail-content { font-size: 15px; line-height: 1.8; color: #333; white-space: pre-wrap; }
.attachments { margin-top: 16px; }
.mt-20 { margin-top: 20px; }
</style>
