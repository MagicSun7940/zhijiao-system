<template>
  <AppLayout :menu-items="menuItems" />
</template>

<script setup>
import AppLayout from '@/components/AppLayout.vue'

const menuItems = [
  { path: '/teacher/posts',           title: '浏览岗位',  icon: 'Search' },
  { path: '/teacher/my-applications', title: '我的申请',  icon: 'List' },
  { path: '/teacher/share',           title: '教学分享',  icon: 'ChatDotRound' },
  { path: '/teacher/share/create',    title: '发布分享',  icon: 'Edit' },
]
</script>
