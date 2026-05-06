<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span>教学资源</span>
        <el-input v-model="keyword" placeholder="搜索" clearable style="width:160px"
          @keyup.enter="loadList" @clear="loadList">
          <template #append><el-button :icon="Search" @click="loadList" /></template>
        </el-input>
      </div>
    </template>

    <div class="share-grid" v-loading="loading">
      <el-card v-for="share in list" :key="share.id" shadow="hover"
        class="share-card" @click="openDetail(share)">
        <div class="share-title">{{ share.title }}</div>
        <div class="share-content">{{ share.content }}</div>
        <div class="share-footer">
          <span><el-icon><View /></el-icon> {{ share.viewCount }}</span>
          <span>{{ share.createTime?.slice(0,10) }}</span>
        </div>
      </el-card>
    </div>
    <el-empty v-if="!loading && list.length === 0" description="暂无教学资源" />

    <el-pagination class="mt-16" v-model:current-page="page" :page-size="10"
      :total="total" layout="total, prev, pager, next" @current-change="loadList" />

    <el-dialog v-model="detailVisible" :title="currentShare?.title" width="92%" style="max-width:680px" top="5vh">
      <div class="detail-content">{{ currentShare?.content }}</div>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Search, View } from '@element-plus/icons-vue'
import { getShareList, getShareDetail } from '@/api/share'
const loading = ref(false), list = ref([]), total = ref(0), page = ref(1), keyword = ref('')
const detailVisible = ref(false), currentShare = ref(null)
async function loadList() {
  loading.value = true
  try { const res = await getShareList({ page: page.value, size: 10, keyword: keyword.value }); list.value = res.data?.records||[]; total.value = res.data?.total||0 }
  finally { loading.value = false }
}
async function openDetail(share) { const res = await getShareDetail(share.id); currentShare.value = res.data; detailVisible.value = true }
onMounted(loadList)
</script>

<style scoped>
.share-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}
.share-card    { cursor:pointer; transition:transform .15s; }
.share-card:hover { transform: translateY(-2px); }
.share-title   { font-size:14px; font-weight:600; color:#1a1a2e; margin-bottom:6px; }
.share-content { font-size:12px; color:#666; line-height:1.6; display:-webkit-box;
  -webkit-line-clamp:3; -webkit-box-orient:vertical; overflow:hidden; }
.share-footer  { display:flex; justify-content:space-between; margin-top:10px; font-size:12px; color:#999; align-items:center; }
.detail-content { font-size:14px; line-height:1.8; color:#333; white-space:pre-wrap; }
.mt-16 { margin-top: 16px; }

@media (max-width: 768px) {
  .share-grid { grid-template-columns: 1fr; }
}
</style>
