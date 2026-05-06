<template>
  <div class="layout-wrapper">

    <!-- ===== 桌面端侧边栏 ===== -->
    <aside class="aside" v-show="!isMobile">
      <div class="logo-area">
        <span class="logo-text">支教管理系统</span>
      </div>
      <el-menu :default-active="activeMenu" router background-color="#001529"
        text-color="#ffffffa6" active-text-color="#ffffff" class="side-menu">
        <el-menu-item v-for="item in menuItems" :key="item.path" :index="item.path">
          <el-icon><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </el-menu-item>
      </el-menu>
    </aside>

    <!-- ===== 右侧主区域 ===== -->
    <div class="main-wrapper">

      <!-- 顶部 Header -->
      <header class="header">
        <span class="page-title">{{ isMobile ? '支教管理系统' : currentTitle }}</span>
        <el-dropdown @command="handleCommand">
          <span class="user-info">
            <el-avatar :size="30" :src="userStore.avatar">
              {{ userStore.realName?.charAt(0) }}
            </el-avatar>
            <span class="user-name">{{ userStore.realName }}</span>
            <el-icon><ArrowDown /></el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </header>

      <!-- 移动端页面标题栏 -->
      <div class="mobile-breadcrumb" v-if="isMobile">
        <el-icon style="font-size:16px;color:#666"><ArrowLeft /></el-icon>
        <span>{{ currentTitle }}</span>
      </div>

      <!-- 主内容区 -->
      <main class="main-content">
        <router-view />
      </main>

      <!-- ===== 移动端底部导航栏 ===== -->
      <nav class="bottom-nav" v-if="isMobile">
        <div
          v-for="item in menuItems.slice(0, 5)"
          :key="item.path"
          class="bottom-nav-item"
          :class="{ active: activeMenu === item.path }"
          @click="$router.push(item.path)">
          <el-icon :size="20"><component :is="item.icon" /></el-icon>
          <span>{{ item.title }}</span>
        </div>
      </nav>
    </div>
  </div>
</template>

<script setup>
import { computed, ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessageBox } from 'element-plus'
import { useUserStore } from '@/store/user'

const props = defineProps({
  menuItems: { type: Array, required: true }
})

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isMobile = ref(window.innerWidth <= 768)

function onResize() {
  isMobile.value = window.innerWidth <= 768
}
onMounted(() => window.addEventListener('resize', onResize))
onUnmounted(() => window.removeEventListener('resize', onResize))

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta?.title || '')

async function handleCommand(cmd) {
  if (cmd === 'logout') {
    await ElMessageBox.confirm('确认退出登录？', '提示', { type: 'warning' })
    await userStore.logout()
    router.push('/login')
  }
}
</script>

<style scoped>
/* ===== 整体布局 ===== */
.layout-wrapper {
  display: flex;
  height: 100vh;
  overflow: hidden;
}

.aside {
  width: 220px;
  min-width: 220px;
  background: #001529;
  display: flex;
  flex-direction: column;
  height: 100vh;
}

.logo-area {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #ffffff1a;
  flex-shrink: 0;
}

.logo-text { color: #fff; font-size: 16px; font-weight: bold; }

.side-menu { border: none; flex: 1; overflow-y: auto; }

.main-wrapper {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

/* ===== Header ===== */
.header {
  height: 56px;
  min-height: 56px;
  background: #fff;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0,21,41,.1);
  flex-shrink: 0;
  z-index: 10;
}

.page-title { font-size: 16px; font-weight: 600; color: #1a1a2e; }

.user-info {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  color: #333;
  font-size: 14px;
}

.user-name {
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* ===== 移动端面包屑 ===== */
.mobile-breadcrumb {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 16px 6px;
  font-size: 13px;
  color: #666;
  background: #f0f2f5;
}

/* ===== 主内容 ===== */
.main-content {
  flex: 1;
  overflow-y: auto;
  background: #f0f2f5;
  padding: 16px;
}

/* ===== 底部导航栏（仅移动端） ===== */
.bottom-nav {
  display: none;
}

/* ===== 移动端媒体查询 ===== */
@media (max-width: 768px) {
  .main-content {
    padding: 12px;
    padding-bottom: 70px; /* 避免被底部导航遮挡 */
  }

  .bottom-nav {
    display: flex;
    height: 56px;
    min-height: 56px;
    background: #fff;
    border-top: 0.5px solid #e8e8e8;
    flex-shrink: 0;
  }

  .bottom-nav-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 2px;
    cursor: pointer;
    color: #999;
    font-size: 11px;
    transition: color 0.2s;
  }

  .bottom-nav-item.active {
    color: #1976d2;
  }

  .bottom-nav-item:active {
    background: #f5f5f5;
  }
}
</style>
